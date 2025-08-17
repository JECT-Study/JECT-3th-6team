import { mkdirSync, writeFileSync } from 'fs';
import { join } from 'path';
import QRCode from 'qrcode';

async function main() {
  const base = 'https://www.spotit.co.kr'.replace(/\/$/, '');
  const fixed = `${base}/qr/feedback`;

  const svg = await QRCode.toString(fixed, {
    type: 'svg',
    errorCorrectionLevel: 'M',
    margin: 2,
    scale: 4,
    color: {
      dark: '#626262',
      light: '#EAEAEA',
    },
  });

  const outDir = join(process.cwd(), 'public/qr');
  mkdirSync(outDir, { recursive: true });
  writeFileSync(join(outDir, 'feedback.svg'), svg, 'utf-8');
  console.log('QR generated: /public/qr/feedback.svg');
}

main().catch(err => {
  console.error(err);
  process.exit(1);
});
