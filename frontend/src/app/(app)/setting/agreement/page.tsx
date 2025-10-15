import PageHeader from '@/shared/ui/header/PageHeader';

export default function Page() {
  return (
    <div>
      <PageHeader title={'약관 및 정책'} />
      <div className="px-[20px] py-[10px] text-base leading-[1.4] select-none">
        <h1 className="text-lg font-semibold py-2 ">이용약관</h1>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제1조 목적
          </h2>
          <p className="text-sm pl-3">
            본 약관은 스팟잇이 제공하는 웨이팅 플랫폼 서비스(이하 ‘서비스’라
            함)의 이용과 관련하여 스팟잇과 이용자 간의 권리, 의무 및 책임사항을
            규정함을 목적으로 합니다.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제2조 용어의 정의
          </h2>
          <p className="text-sm pl-3">
            본 약관에서 사용하는 용어의 정의는 다음과 같습니다.
          </p>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3 mt-2">
            <li>
              서비스: 스팟잇이 제공하는 팝업/플리마켓 정보 제공, 웨이팅 등록
              관리, 실시간 대기 현황 확인 등을 위한 온라인 플랫폼 서비스를
              의미합니다. 본 서비스는 구현되는 장치에 상관없이 웹(Web), 앱(App)
              환경에서 이용할 수 있습니다.
            </li>
            <li>
              이용자: 서비스에 접속하여 본 약관에 따라 스팟잇에서 제공하는
              서비스를 이용하는 자를 말하며, 회원가입 여부와 관계없이 모든
              방문자를 포함합니다. 회원계정(ID/PW)을 생성하지 않은 일반
              고객(일명 비회원 고객)도 포함됩니다.
            </li>
            <li>
              회원: 카카오 간편로그인을 통해 서비스에 회원가입을 완료한 자로서,
              회사가 제공하는 회원 전용 서비스인 웨이팅 등록, 방문 내역 조회
              등을 지속적으로 이용할 수 있는 자를 말합니다.
            </li>
            <li>
              비회원: 회원가입 절차 없이 스팟잇이 제공하는 일반적인 서비스인
              팝업스토어 조회, 검색 등을 이용하는 자를 말합니다.
            </li>
            <li>
              스토어: 스팟잇 플랫폼 내에서 웨이팅 정보가 등록 및 운영되는
              개별적인 행사를 말합니다. 현재는 팝업스토어와 플리마켓을
              포함합니다.
            </li>
            <li>
              웨이팅: 이용자가 팝업스토어 방문을 위해 스팟잇 플랫폼 내에서
              웨이팅 순서를 등록하는 기능을 말합니다.
            </li>
          </ol>
          <p className="text-sm pl-3 mt-2">
            본 약관에서 사용되는 용어 중 본 조에서 정의하지 아니한 것은 관련
            법령 및 일반 상관례에 따릅니다.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제3조 약관의 게시와 개정
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 본 약관의 내용을 이용자가 쉽게 알 수 있도록 서비스
              연결화면을 통하여 게시합니다.
            </li>
            <li>
              스팟잇은 관련 법령을 준수하며 본 약관을 개정할 수 있으며, 개정 시
              적용 일자 및 개정 사유를 명시하여 개정 약관의 적용일 7일 전부터
              서비스 내 공지사항을 통해 안내합니다. 단, 이용자에게 불리한 약관
              개정의 경우에는 최소 30일 전에 공지합니다.
            </li>
            <li>
              이용자가 개정 약관에 대해 명시적으로 거부의 의사를 표명하지 않고
              서비스를 계속 이용하는 경우, 개정된 약관에 동의한 것으로
              간주합니다.
            </li>
            <li>
              이용자는 개정된 약관에 대해 동의하지 않을 권리가 있으며 이용자가
              개정된 약관에 동의하지 않을 경우, 서비스 이용을 중단하고 탈퇴할 수
              있습니다.
            </li>
            <li>
              본 약관은 이용자가 약관의 내용에 동의함으로써 효력이 발생하여
              이용계약 종료일까지 적용됩니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제4조 이용자에 대한 통지
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇 본 약관에 별도 규정이 없는 한 이용자가 등록한 이메일,
              서비스 내 공지사항 등으로 통지할 수 있습니다. 이용자가
              이메일주소를 정확히 등록하지않거나 변경된 정보를 수정 또는 회사에
              알리지 않은 경우 회사는 이용자가 등록한 정보로 발송한 때에
              이용자에게 통지된 것으로 간주합니다.
            </li>
            <li>
              이용자 전체에 대한 통지의 경우, 7일 이상 스팟잇의 공지사항에
              게시함으로서 통지를 대신할 수 있습니다. 단, 이용자의 서비스 이용에
              중대하게 불리한 영향을 주는 사항에 대해서는 이메일로의통지를
              병행합니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제5조 이용 계약의 성립
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              이용계약은 이용자가 약관의 내용에 대하여 동의하고 스팟잇이
              제시하는 양식과 절차에 따라 이용신청을 하고 스팟잇이 이를
              승인함으로써 성립합니다.
            </li>
            <li>
              제1항 신청에 있어 스팟잇은 카카오 회원가입을 진행하며 회원가입에
              필요한 최소한의 정보인 닉네임, 카카오계정(이메일)을 제공해야
              합니다.
            </li>
            <li>
              스팟잇은 서비스 제공을 위한 설비가 부족하거나 기술상 또는 업무상
              문제가 있는 경우에는 승인을 유보할 수 있습니다.
            </li>
            <li>
              이용자는 서비스 이용을 위해 허위 사실이 없는 진실되고 정확한
              정보를 제공해야 하며, 스팟잇은 제공하는 서비스에 따라 필요한 경우
              이용자에게 추가 정보를 요청할 수 있습니다.
            </li>
            <li>
              스팟잇과 이용자가 서비스 이용과 관련해 별도 계약을 체결한 경우,
              해당 별도 계약이 본 약관에 우선하여 적용됩니다.
            </li>
            <li>
              스팟잇은 이용자의 서비스 이용 신청에 대하여 서비스 이용 승낙을
              원칙으로 합니다. 단, 다음과 같은 경우 승인을 거부할 수 있습니다.
            </li>
            <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
              <li>
                이용자가 본 약관에 의하여 이전에 서비스 이용자격을 상실한 적이
                있는 경우 (단, 재가입 승낙을 얻은 경우에는 예외로 함)
              </li>
              <li>실명이 아니거나 타인의 명의를 이용한 경우</li>
              <li>허위 정보 기재 또는 필수 기재 사항 누락</li>
              <li>만 14세 미만의 신청자</li>
              <li>
                서비스의 정상적인 제공을 방해하거나 다른 이용자의 서비스 이용에
                지장을 줄 것으로 예상되는 경우
              </li>
              <li>이용자의 귀책사유로 인하여 승인이 불가능한 경우</li>
              <li>관련 법령·사회질서에 반할 우려가 있는 경우</li>
            </ul>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제6조 개인정보 수집
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 이용계약의 성립 및 이행에 필요한 최소한의 개인정보를
              수집합니다.
            </li>
            <li>
              스팟잇은 개인정보의 수집 시 관련 법규에 따라 개인정보 처리방침에
              그 수집범위 및 목적을 사전 고지합니다.
            </li>
            <li>
              스팟잇은 서비스 화면에서 회사가 수집한 개인정보의 수집, 이용 또는
              제공에 대한 동의를 철회할 수 있도록 필요한 조치를 취해야 합니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제7조 개인정보보호 의무
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 개인정보보호법, 정보통신망 이용촉진 및 정보보호 등에 관한
              법률 등 관계 법령이 정하는 바에 따라 이용자의 개인정보를 보호하기
              위해 노력합니다.
            </li>
            <li>
              개인정보의 보호 및 이용에 대해서는 관련법 및 스팟잇의 개인정보
              처리방침이 적용됩니다. 단, 스팟잇의 공식사이트 이외의 링크된
              사이트에서는 스팟잇의 개인정보 처리방침이 적용되지 않습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제8조 계정 관리에 대한 의무
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 카카오 로그인 등 외부 서비스를 통해 이용자의 계정을
              확인하며, 이용자는 해당 서비스의 계정 및 비밀번호를 안전하게
              관리할 책임이 있습니다.
            </li>
            <li>
              외부 로그인 서비스에서 발생하는 계정 도용, 비밀번호 유출, 인증
              오류 등에 대해 스팟잇은 책임을 지지 않습니다.
            </li>
            <li>
              단, 이용자는 자신의 계정이 제3자에 의해 부정하게 사용됨을 인지한
              경우, 즉시 스팟잇에 통지하고 스팟잇의 안내를 따라야 하며, 스팟잇은
              필요한 범위 내에서 지원 조치를 제공합니다. 스팟잇에 제3자 도용,
              부정사용 사실 등을 통지하지 않거나 통지하였으나 스팟잇의 안내를
              따르지 않아 발생한 불이익에 대해서 스팟잇은 책임지지 않습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제9조 스팟잇의 의무
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 관련 법령과 본 약관을 준수하며, 건전한 서비스 운영을 위해
              노력합니다.
            </li>
            <li>
              스팟잇은 이용자가 안전하게 서비스를 이용할 수 있도록 보안 시스템을
              운영하며, 개인정보 처리방침을 공시하고 준수합니다.
            </li>
            <li>
              스팟잇은 이용자의 개인정보를 이용자의 동의 없이 제 3자에게
              제공하지 않으며, 법령에 의한 관계기관으로부터의 요청 등 법률의
              규정에 따른 적법한 절차에 의한 경우에는 예외로 합니다.
            </li>
            <li>
              스팟잇은 이용자에게 가능한 한 안정적으로 서비스를 제공하기 위해
              최선을 다합니다. 단, 기술적 문제, 점검, 천재지변 등 불가피한
              사유로 서비스가 중단될 수 있습니다. 이 경우 지체없이 이를 수리
              또는 복구하기 위해 노력하며, 서비스 내 공지사항 게시, 이메일 등을
              사전 또는 사후적으로 이를 이용자에게 고지합니다.
            </li>
            <li>
              스팟잇은 이용자의 문의나 불만을 공식 고객센터인 ‘스팟잇’ 카카오톡
              채널을 통해 접수하며, 제기된 의견이나 불만이 정당하다고 인정될
              경우, 이를 합리적인 기간 내에 처리하여 이용자에게 통보합니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제10조 이용자의 의무
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              이용자는 서비스 이용 시 관련 법령, 본 약관, 스팟잇의 공지사항을
              준수하여야 합니다.
            </li>
            <li>
              이용자는 다음 행위를 해서는 안됩니다.
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>
                  서비스 이용 또는 웨이팅 등록 시 허위내용을 입력하는 행위
                </li>
                <li>타인의 계정을 도용하거나 명의를 도용하는 행위</li>
                <li>
                  스팟잇 내 제공되는 콘텐츠, 공지사항, 이미지 등을 무단으로
                  복제·수정·배포하는 행위
                </li>
                <li>스팟잇의 명예를 손상시키거나 업무를 방해하는 행위</li>
                <li>
                  스팟잇의 사이트나 데이터베이스가 보관된 서버에의 접근을
                  제한하기 위한 보호조치를 침해하는 행위
                </li>
                <li>기타 불법적이거나 부당한 행위</li>
              </ul>
            </li>
            <li>
              이용자가 제 2항의 첫번째 행위를 하여 스팟잇 또는 제3자에게 손해를
              끼친 경우, 이용자는 그에 대한 모든 법적 책임을 부담하며, 스팟잇은
              이에 대해 책임을 지지 않습니다.
            </li>
            <li>
              이용자는 스팟잇의 명시적인 동의가 없는 한 서비스의 이용권한, 기타
              이용계약상 지위를 타인에게 양도, 증여, 담보로 제공하는 등 기타
              이와 유사한 행위를 할 수 없습니다.
            </li>
            <li>
              이용자는 회사의 정상적인 서비스 운영을 방해하거나 관계 법령 및 본
              약관에서 금지하는 행위를 하여서는 안됩니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제11조 불만처리
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 이용자의 의견을 소중히 여기며, 서비스 이용과 관련된 문의
              또는 불만사항이 접수되는 경우 신속하고 성실하게 대응합니다.
            </li>
            <li>
              이용자는 스팟잇 카카오톡 채널을 통해 의견이나 불만을 제출할 수
              있습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제12조 서비스의 제공 및 변경
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 팝업스토어 웨이팅 등 이용자에게 제공하는 주요 기능을 웹
              내에서 제공합니다.
            </li>
            <li>
              스팟잇은 서비스 품질 향상 및 기능 개선을 위해 주기적으로 점검 및
              업데이트를 실시할 수 있으며, 정기점검 시간은 서비스 내 공지사항을
              통해 사전에 안내합니다.
            </li>
            <li>
              스팟잇은 더 나은 서비스의 제공을 위하여 이용자에게 서비스의 이용과
              관련된 각종 고지, 관리 메시지 및 기타 광고를 비롯한 다양한 정보를
              서비스 화면 내에 표시할 수 있습니다.
            </li>
            <li>
              서비스 제공의 중단이나 변경이 불가피한 경우, 관계 법령에 특별한
              규정이 없는 한 이용자에게 별도의 보상은 제공하지 않습니다.
            </li>
            <li>
              스팟잇은 이용자의 서비스 이용과 관련된 중요 안내사항이나 업데이트
              소식 등을 웹사이트 내 공지사항 등으로 전달할 수 있습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제13조 서비스의 중단
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 다음과 같은 사유로 서비스 제공을 일시적으로 중단할 수
              있습니다:
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>설비 점검, 보수, 교체 등으로 인한 경우</li>
                <li>통신서비스의 장애 또는 서버 과부하 등으로 인한 경우</li>
                <li>천재지변, 비상사태 등 불가항력적 사유로 인한 경우</li>
                <li>
                  팝업스토어 주최자(입점 브랜드 또는 운영자)의 요청이나 내부
                  사정으로 인해 서비스 기능 제공이 불가능한 경우
                </li>
              </ul>
            </li>
            <li>
              스팟잇은 서비스 중단에 대해 사전에 통지하며, 불가피한 사유로 사전
              통지가 어려운 경우 사후에 통지할 수 있습니다.
            </li>
            <li>
              스팟잇은 서비스 중단으로 발생하는 손해에 대해서는 고의 또는 중대한
              과실이 없는 한 책임을 지지 않습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제14조 서비스 이용의 제한 및 정지
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 이용자가 본 약관의 의무를 위반하거나 서비스의 정상적인
              운영을 방해한 경우, 서비스 이용을 제한하거나 정지할 수 있습니다.
            </li>
            <li>
              이용자가 다음 각 호의 행위에 해당하는 행위가 적발되어 즉시 서비스
              이용이 제한되거나 정지되는 경우 서비스 내 혜택 및 권리 등도 모두
              소멸됩니다. 이에 대해 스팟잇은 별도로 보상하지 않으며 이용자는
              법적 제재를 받을 수 있습니다.
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>명의 도용</li>
                <li>약관 위반 또는 법령 위반 행위</li>
                <li>
                  허위정보를 등록하거나 부정확한 웨이팅 등록을 반복한 경우
                </li>
                <li>타인의 명예를 훼손하거나 권리를 침해하는 행위</li>
                <li>
                  서비스의 기술적 보호 조치를 무력화하거나, 시스템에 비인가
                  접근을 시도한 경우
                </li>
                <li>
                  서비스에서 정상적으로 부여한 접속권한을 우회하거나 초과하려는
                  행위
                </li>
                <li>
                  서비스의 오작동 또는 버그를 의도적으로 유도하거나 이용하는
                  행위
                </li>
                <li>그 밖에 이용자의 고의 또는 중과실로 인한 위법 행위</li>
              </ul>
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제15조 계약해지
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              이용자는 이용계약을 해지 하고자 할 때 본인이 직접 웹사이트에서
              탈퇴를 신청해야 합니다. 탈퇴 시, 방문 내역은 즉시 소멸되며
              복구되지 않습니다.
            </li>
            <li>
              스팟잇은 이용자가 다음 각 호에 해당할 경우에는 이용자의 동의 없이
              이용계약을 해지할 수 있으며 그 사실을 이용자에게 통지합니다. 단,
              회사가 긴급하게 해지할 필요가 있다고 인정하는 경우나 이용자의
              귀책사유로 인하여 통지할 수 없는 경우에는 지체없이 사후 통지로
              대체할 수 있습니다.
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>
                  이용자가 제10조와 제14조를 포함한 본 약관을 위반하고 회사가
                  안내한 일정기간 이내에 위반 내용을 해소하지 않은 경우
                </li>
                <li>
                  스팟잇의 서비스 제공 목적 외의 용도로 서비스를 이용한 경우
                </li>
                <li>
                  제14조 ‘서비스 이용의 제한 및 정지’ 규정에 의하여 이용정지를
                  당한 이후 1년 이내에 이용정지 사유가 재발한 경우
                </li>
              </ul>
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제16조 데이터의 보존 및 파기
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 서비스 제공에 필요한 범위 내에서 이용자의 웨이팅 정보,
              방문 내역, 이용 이력 등의 데이터를 보존할 수 있습니다.
            </li>
            <li>
              이용자는 서비스 이용 중 생성된 데이터가 필요한 경우, 직접 화면을
              캡쳐하는 등을 통해 별도로 저장 및 보관하여야 하며, 스팟잇이
              이용자의 데이터를 백업하거나 이용자에게 제공할 의무는 없습니다.
              이용자가 이를 소홀히 하여 발생한 데이터의 유실 및 누락에 대해서는
              스팟잇은 책임을 지지 않습니다.
            </li>
            <li>
              서비스 이용기간 만료, 서비스 이용계약의 해제 또는 해지 등의 사유로
              회사가 서비스 제공을 종료한 경우 관계법령에 따라 일정 기간 보존이
              필요한 정보를 제외하고는 이용자의 데이터를 지체 없이 파기합니다.
            </li>
            <li>
              스팟잇은 다음과 같은 정보를 법령에 따라 정해진 기간 동안 보존할 수
              있습니다.
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>계약 또는 청약철회 등에 관한 기록</li>
                <li>소비자의 불만 또는 분쟁처리에 관한 기록</li>
              </ul>
            </li>
          </ol>
          <p className="mt-4 text-xs italic text-gray-500 ml-4">
            ※ 개인정보 파기와 관련된 자세한 내용은 개인정보 처리방침에서
            확인하실 수 있습니다.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제17조 게시물의 저작권
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>서비스 자체에 대한 지식재산권은 스팟잇에 귀속됩니다.</li>
            <li>
              이용자는 서비스를 이용하여 얻은 정보를 가공, 판매하는 행위 등
              게재된 데이터를 상업적으로 이용할 수 없으며 이를 위반하여 발생하는
              제반 문제에 대한 책임은 이용자에게 있습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제18조 손해배상의 범위 및 청구
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇이 고의 또는 과실로 이용자에게 손해를 끼친 경우, 손해에
              대하여 배상할 책임이 있습니다. 단, 시스템 장애 등 불가항력적인
              사유로 인한 서비스 중단, 오류 등으로 발생한 손해에 대해서는 책임을
              지지 않습니다.
            </li>
            <li>
              스팟잇은 이용자가 본 약관의 규정을 위반함으로 인하여 회사에 손해가
              발생하게 되는 경우, 본 약관을 위반한 이용자는 회사에 발생하는 모든
              손해를 배상하여야 합니다.
            </li>
            <li>
              이용자가 서비스와 관련하여 스팟잇을 상대로 손해배상을 청구하고자
              하는 경우, 그 사유가 발생한 날로부터 3개월 이내에 스팟잇에
              서면으로 청구하여야 합니다.
            </li>
            <li>
              스팟잇 및 타인에게 피해를 주어 피해자의 고발 또는 소송 제기로
              인하여 손해배상이 청구된 이용자는 스팟잇 및 수사기관의 요청에 적극
              협조하여야 합니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제19조 면책
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>
              스팟잇은 다음 각 호의 경우로 서비스를 제공할 수 없는 경우 이로
              인하여 이용자 또는 제 3자에게 발생한 손해에 대해서는 책임을 지지
              않습니다.
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>
                  천재 지변 또는 이에 준하는 불가항력적인 사유가 있는 경우
                </li>
                <li>
                  이용자의 시스템 환경이나 통신사 또는 호스팅업체의 장애 등
                  회사의 관리 범위 밖의 사정으로 인한 경우
                </li>
                <li>
                  현재의 보안기술 수준으로는 방어가 곤란한 네트워크 해킹 등으로
                  인한 경우
                </li>
                <li>
                  서비스 개선, 시스템 점검, 장비 교체 등으로 인해 사전에 공지된
                  서비스 중단이 이루어지는 경우
                </li>
                <li>
                  이용자의 부주의 또는 귀책사유로 인해 정보가 유출되거나 서비스
                  이용에 장애가 발생한 경우
                </li>
                <li>회사의 고의 및 과실이 없는 경우</li>
              </ul>
            </li>
            <li>
              스팟잇은 이용자가 서비스를 통해 얻은 정보 또는 자료 등을
              신뢰하거나 활용함으로써 발생한 손해에 대해 책임을 지지 않습니다.
            </li>
            <li>
              스팟잇은 이용자가 전송한 데이터의 내용에 대해서는 책임을 면합니다.
            </li>
            <li>
              스팟잇은 무료로 제공하는 서비스 이용과 관련하여 관련법에 특별한
              규정이 없는 한 책임을 지지 않습니다.
            </li>
            <li>
              스팟잇은 이용자가 서비스 이용 종료 후 제16조에 따라 파기된
              데이터와 이용자가 직접 삭제를 요청한 데이터 기타 서비스 이용
              중인지 여부를 불문하고 백업의무를 소홀히 하여 발생한 데이터의
              손실에 대해서는 책임을 지지 않습니다.
            </li>
            <li>
              스팟잇은 서비스를 통하여 이용자에게 제공된 정보, 자료, 사실의
              신뢰도, 정확성 등 그 내용에 대해서는 책임을 지지 않습니다.
            </li>
          </ol>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제20조 분쟁조정
          </h2>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-3">
            <li>본 약관은 대한민국법령에 의하여 규정되고 이행됩니다.</li>
            <li>
              회사와 이용자 간 서비스 이용과 관련하여 발생한 분쟁은 상호 협의를
              통해 원만히 해결하는 것을 원칙으로 합니다.
            </li>
            <li>
              협의로 해결되지 아니한 경우, 관련 법령에 따라 민사소송법상의
              관할법원을 전속 관할 법원으로 합니다.
            </li>
            <li>
              해외에 주소 또는 거소를 둔 이용자의 경우, 회사와의 분쟁에 대한
              관할 법원은 대한민국 서울중앙지방법원으로 합니다.
            </li>
          </ol>
        </section>

        <section className="mb-6 mt-8 pt-4 ">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-sub pl-3">
            [부칙]
          </h2>
          <p className="text-sm pl-3 font-semibold text-gray-800">
            (시행일) 이 약관은 2025년 10월 24일부터 시행합니다.
          </p>
        </section>

        <hr className="my-4 border-gray-200" />

        <h1 className="text-lg font-semibold py-2 ">개인정보처리방침</h1>

        <p className="text-sm pl-3 mb-6">
          스팟잇은 정보주체의 자유와 권리 보호를 위해 「개인정보 보호법」 및
          관계 법령이 정한 바를 준수하여, 적법하게 개인정보를 처리하고 안전하게
          관리하고 있습니다. 이에 「개인정보 보호법」 제30조에 따라 정보주체에게
          개인정보의 처리와 보호에 관한 절차 및 기준을 안내하고, 이와 관련한
          고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이
          개인정보 처리방침을 수립・공개합니다.
        </p>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제1조 개인정보의 처리 목적
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 다음의 목적을 위하여 회원가입을 통해 제공받은 개인정보를
            처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는
            이용되지 않으며, 이용 목적이 변경되는 경우에는 「개인정보 보호법」
            제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.
          </p>

          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6 mt-3">
            <li>
              <strong>회원 가입 기능:</strong> 회원 관리, 회원 맞춤 안내 제공에
              따른 본인 식별, 통계학적 특성에 따른 서비스 제공 목적으로
              개인정보를 처리합니다.
            </li>
            <li>
              <strong>알림 서비스 제공:</strong> 현장 줄서기 알림 기능 제공,
              팝업스토어 사전 예약 알림 기능 제공, 맞춤형 정보 제공, 신규 기능
              및 컨텐츠 정보 제공의 목적으로 개인정보를 처리합니다.
              <p className="text-xs  text-gray-500 mt-1 ml-4">
                (예. 팝업스토어 웨이팅 확정 안내, 웨이팅 순서 4번째 팀 알림,
                입장 시간 초과 알림 등)
              </p>
            </li>
          </ul>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제2조 수집하는 개인정보 항목
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 다음과 같은 방법으로 개인정보를 수집합니다.
          </p>

          <h3 className="text-sm font-semibold pl-3 mt-3">1. 수집 항목</h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>회원 가입 및 관리 기능 : 닉네임, 카카오계정(이메일)</li>
            <li>웨이팅 걸기 기능 : 이름, 함께 웨이팅하는 인원수, 이메일</li>
            <li>
              자동 수집 항목 : 서비스 이용 기록, 접속 로그, 쿠키, 기기 정보 등
            </li>
          </ul>

          <h3 className="text-sm font-semibold pl-3 mt-3">2. 처리 방법</h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>회원가입 및 서비스 이용 과정에서 이용자가 직접 입력</li>
            <li>소셜 로그인을 통한 자동 수집</li>
          </ul>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제3조 개인정보의 처리 및 보유 기간
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 법령에 따른 개인정보 보유·이용 기간 또는 정보주체로부터
            개인정보를 수집 시에 동의 받은 개인정보 보유·이용기간 내에서
            개인정보를 수집·처리·보유합니다.
          </p>

          <div className="overflow-x-auto rounded-lg border border-gray-200 mt-3 mx-3">
            <table className="terms-table">
              <thead className="bg-indigo-500 text-white">
                <tr>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    구분
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    보존 항목
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    보존 기간
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    법령
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                <tr>
                  <td>이용자 식별 및 로그인 기록</td>
                  <td>회원 식별정보(카카오ID, 이름,성별) 로그인 기록</td>
                  <td>회원 탈퇴 시까지</td>
                  <td>개인정보보호법</td>
                </tr>
                <tr>
                  <td>서비스 이용 관련 기록</td>
                  <td>웨이팅 및 방문 내역</td>
                  <td>1년</td>
                  <td>개인정보보호법</td>
                </tr>
                <tr>
                  <td>소비자 불만 또는 문의 대응 기록</td>
                  <td>카카오톡 채널 문의, 이메일 문의</td>
                  <td>3년</td>
                  <td>전자상거래 등에서의 소비자 보호에 관한 법률</td>
                </tr>
                <tr>
                  <td>웹사이트/앱 접속 기록</td>
                  <td>접속 IP, 접속 일시, 기기 정보, 브라우저 정보</td>
                  <td>3개월</td>
                  <td>통신비밀보호법</td>
                </tr>
                <tr>
                  <td>부정 이용 방지 관련 기록</td>
                  <td>비정상 접근, 계정 도용 시도 등</td>
                  <td>1년</td>
                  <td>개인정보보호법</td>
                </tr>
                <tr>
                  <td>분석용 로그 데이터</td>
                  <td>접속 빈도, 스토어별 웨이팅 수 등</td>
                  <td>1년</td>
                  <td>개인정보보호법</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제4조 개인정보의 파기절차 및 방법
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 개인정보 보유기간의 경과 및 개인정보 처리 목적이 달성되어
            개인정보가 불필요하게 되었을 때에는 지체없이 해당 정보를 파기합니다.
            다만, 관련 법령에 따라 일정 기간 보관해야 하는 정보는 법령에서 정한
            기간 동안 안전하게 분리하여 보관합니다.
          </p>

          <h3 className="text-sm font-semibold pl-3 mt-3">파기 절차</h3>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>
              이용자가 회원가입 등을 위해 입력한 개인정보는 목적이 달성된 후
              내부 방침 및 기타 관련 법령에 의한 정보보호 사유에 따라 일정 기간
              저장된 후 파기됩니다.
            </li>
            <li>
              스팟잇은 법령에서 별도 보관을 요구하지 않는 이상, 회원 탈퇴 시점에
              즉시 개인정보를 파기합니다.
            </li>
          </ol>

          <h3 className="text-sm font-semibold pl-3 mt-3">파기 방법</h3>
          <ol className="list-decimal list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>
              전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을
              사용하여 삭제합니다.
            </li>
            <li>종이 문서에 기록된 정보는 분쇄기로 파쇄하거나 소각합니다.</li>
          </ol>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제5조 개인정보의 제3자 제공
          </h2>
          <p className="text-sm pl-3">
            회사는 이용자의 별도 동의가 있는 경우나 법령에서 규정된 경우를
            제외하고 이용자의 개인정보를 제3자에게 제공하지 않습니다.
          </p>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제6조 개인정보 처리의 위탁
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 서비스 향상을 위해 개인정보 처리를 외부에 위탁할 수 있으며,
            위탁 업무의 내용이나 수탁자가 변경될 경우, 본 개인정보 처리방침을
            통하여 공개하도록 하겠습니다.
          </p>

          <div className="overflow-x-auto rounded-lg border border-gray-200 mt-3 mx-3">
            <table className="terms-table">
              <thead className="bg-indigo-500 text-white">
                <tr>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    수탁업체
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    위탁 업무 내용
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    위탁 항목
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                <tr>
                  <td>
                    <strong>㈜카페24</strong>
                  </td>
                  <td>
                    웹사이트 및 데이터베이스 서버 운영, 서비스 인프라 제공
                  </td>
                  <td>서비스 이용기록, 접속 로그, 쿠키, IP 정보 등</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제7조 개인정보의 국외 수집 및 이전
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 서비스 품질 향상 및 사용자 경험 분석을 위하여 아래와 같이
            국외 사업자의 분석 도구를 이용하고 있습니다. 이 과정에서 일부
            이용자의 서비스 이용 정보가 해외 서버로 전송될 수 있습니다. 스팟잇은
            관련 법령을 준수하며, 각 사업자의 개인정보 처리방침을 확인할 수
            있도록 안내합니다.
          </p>

          <div className="overflow-x-auto rounded-lg border border-gray-200 mt-3 mx-3">
            <table className="terms-table">
              <thead className="bg-indigo-500 text-white">
                <tr>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    수탁업체
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    위탁 목적
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    위탁 항목
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    위탁 국가 및 연락처
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    위탁 방법
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                <tr>
                  <td>
                    <strong>Google LLC</strong> (Google Analytics, Google Tag
                    Manager)
                  </td>
                  <td>이용자의 서비스 이용 행태 분석 및 서비스 개선</td>
                  <td>서비스 이용기록 등</td>
                  <td>
                    미국 /{' '}
                    <a
                      href="mailto:googlekrsuppport@google.com"
                      className="text-indigo-600 hover:text-indigo-800 underline"
                    >
                      googlekrsuppport@google.com
                    </a>
                  </td>
                  <td>서비스 이용 시 네트워크를 통해 자동 전송</td>
                </tr>
                <tr>
                  <td>
                    <strong>Microsoft Corporation</strong> (Clarity)
                  </td>
                  <td>이용자 행태 분석 및 서비스 개선</td>
                  <td>접속 로그, 이용 행태정보 등</td>
                  <td>
                    미국 /{' '}
                    <a
                      href="mailto:privacy@support.microsoft.com"
                      className="text-indigo-600 hover:text-indigo-800 underline"
                    >
                      privacy@support.microsoft.com
                    </a>
                  </td>
                  <td>서비스 이용 시 네트워크를 통해 자동 전송</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제8조 개인정보의 안전성 확보 조치
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 이용자의 개인정보가 분실, 도난, 유출, 위조, 훼손되지 않도록
            다음과 같은 조치를 취하고 있습니다.
          </p>

          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6 mt-3">
            <li>
              <strong>관리적 조치:</strong> 개인정보 보호책임자 지정 및 접근
              권한 관리, 개인정보 취급자에 대한 보안 의식 안내
            </li>
            <li>
              <strong>기술적 조치:</strong> 서비스 접속 기록의 주기적 점검 및
              접근 권한 제한, 고유 식별 정보 등의 암호화, 외부 보안 시스템 이용
            </li>
            <li>
              <strong>물리적 조치:</strong> 웹호스팅 서버에 대한 접근 제한
            </li>
          </ul>
          <p className="text-sm pl-3 mt-3">
            위 조치 외에도 서비스 운영 환경의 보안 수준을 지속적으로 점검하고
            개선하기 위해 노력하고 있습니다.
          </p>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제9조 쿠키 사용에 관한 사항
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 이용자에게 보다 나은 서비스와 사용자 경험을 제공하기 위하여
            쿠키(cookie)를 사용합니다. 쿠키는 웹사이트를 운영하는 데 이용되는
            서버가 이용자의 컴퓨터 브라우저로 전송하는 소량의 정보로, 이용자의
            기기에 저장될 수 있습니다.
          </p>

          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6 mt-3">
            <li>
              <strong>쿠키의 사용목적:</strong> 서비스 접속 및 이용 편의성 향상,
              이용자 접속 빈도 또는 방문 시간 등 이용 형태 분석, 카카오 로그인
              등 외부 연동 서비스 제공
            </li>
            <li>
              <strong>쿠키의 설치·운영 및 거부:</strong> 이용자는 아래의 경로를
              통해 쿠키 저장을 거부할 수 있습니다. 쿠키 저장을 거부할 경우
              맞춤형 서비스 이용에 어려움이 발생할 수 있습니다.
              <ul className="list-disc list-inside space-x-4 mt-1 ml-4 text-sm">
                <li>1. Chrome : 설정 → 개인정보 및 보안 → 서드파티쿠키</li>
                <li>2. Edge : 설정 → 개인정보, 검색 및 서비스 → 쿠키</li>
              </ul>
            </li>
          </ul>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제10조 행태정보의 수집·운영·제공에 관한 사항
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 서비스 이용과정에서 정보주체에게 최적화된 맞춤형 서비스 및
            혜택 등을 제공하기 위하여 행태정보를 수집·이용하고 있습니다.
          </p>

          <div className="overflow-x-auto rounded-lg border border-gray-200 mt-3 mx-3">
            <table className="terms-table">
              <thead className="bg-indigo-500 text-white">
                <tr>
                  <th scope="col" className="py-3 px-4 font-semibold w-1/4">
                    구분
                  </th>
                  <th scope="col" className="py-3 px-4 font-semibold">
                    내용
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                <tr>
                  <td>
                    <strong>수집하는 행태정보의 항목</strong>
                  </td>
                  <td>
                    이용자의 서비스 내 방문 기록, 활동 로그 및 검색 이력 등
                  </td>
                </tr>
                <tr>
                  <td>
                    <strong>행태정보 수집 방법</strong>
                  </td>
                  <td>
                    • 쿠키 설치 및 운영 <br></br>• 웹 사이트 방문 시 생성 정보
                    수집 툴 등을 통해 자동 수집 전송
                  </td>
                </tr>
                <tr>
                  <td>
                    <strong>사용하는 수집 툴</strong>
                  </td>
                  <td>Google Analytics, Clarity</td>
                </tr>
                <tr>
                  <td>
                    <strong>행태정보 수집 목적</strong>
                  </td>
                  <td>
                    이용자 행태정보를 분석하여 신규 서비스, 기존 서비스 개선
                    등의 척도로 활용
                  </td>
                </tr>
                <tr>
                  <td>
                    <strong>보유, 이용기간 및 이후 정보 처리 방법</strong>
                  </td>
                  <td>행태정보를 수집일로부터 1년 동안 보유한 후 파기</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제11조 정보주체와 법정대리인의 권리와 의무 및 행사방법
          </h2>
          <p className="text-sm pl-3">
            이용자는 스팟잇에 개인정보를 제공한 경우, 언제든지 다음과 같은
            권리를 행사할 수 있습니다.
          </p>

          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6 mt-3">
            <li>개인정보 열람 요구</li>
            <li>개인정보 정정·삭제 요구</li>
            <li>개인정보 처리정지 요구</li>
            <li>
              권리행사는 회사에 대해 개인정보 보호법 시행령 제41조 제1항에 따라
              이메일, 스팟잇 카카오톡 문의 채널 등을 통해 하실 수 있으며,
              스팟잇은 이에 따라 지체없이 조치하겠습니다.
            </li>
            <li>
              이용자는 법정대리인이나 위임을 받은 자를 통해 권리를 행사할 수
              있으며, 이 경우 별도의 위임장을 제출하셔야 합니다.
            </li>
            <li>
              개인정보 열람 및 처리정지 요구는 개인정보 보호법 제35조 제4항,
              제37조 제2항에 의하여 정보주체의 권리가 제한될 수 있습니다.
            </li>
            <li>
              개인정보의 정정 및 삭제 요구는 다른 법령에서 그 개인정보가 수집
              대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.
            </li>
            <li>
              스팟잇은 정보주체 권리행사 요청 시 요청자가 본인이거나 정당한
              대리인인지를 확인합니다.
            </li>
          </ul>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제12조 개인정보 보호책임자
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 개인정보 처리에 관하여 이용자의 권리 보호 및 불만 처리 등을
            담당할 책임자를 지정하고 있으며, 관련 문의가 필요할 경우 아래로
            연락해 주시기바랍니다.
          </p>

          <div className="text-sm pl-6 mt-3 space-y-1 bg-indigo-50 p-4 rounded-lg">
            <p>
              <strong>성명:</strong> 손혜림
            </p>
            <p>
              <strong>직위:</strong> CEO
            </p>
            <p>
              <strong>이메일:</strong>{' '}
              <a
                href="mailto:0spotit0@gmail.com"
                className="text-indigo-600 hover:text-indigo-800 underline"
              >
                0spotit0@gmail.com
              </a>
            </p>
            <p>
              <strong>문의:</strong> 카카오톡 ‘스팟잇’ 채널
            </p>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제13조 권익침해 구제방법
          </h2>
          <p className="text-sm pl-3">
            정보주체는 스팟잇에서 개인정보와 관련해 권익침해가 발생한 경우, 아래
            기관을 통해 분쟁 해결, 신고, 상담 등을 신청할 수 있습니다.
          </p>

          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6 mt-3">
            <li>
              <strong>개인정보분쟁조정위원회:</strong> 1833-6972{' '}
              <a
                href="http://www.kopico.go.kr/"
                className="text-indigo-600 hover:text-indigo-800 underline"
                target="_blank"
              >
                www.kopico.go.kr
              </a>
            </li>
            <li>
              <strong>개인정보침해신고센터 (KISA):</strong> 118{' '}
              <a
                href="https://privacy.kisa.or.kr/"
                className="text-indigo-600 hover:text-indigo-800 underline"
                target="_blank"
              >
                privacy.kisa.or.kr
              </a>
            </li>
            <li>
              <strong>대검찰청 사이버범죄수사부:</strong> 1301{' '}
              <a
                href="http://www.spo.go.kr/"
                className="text-indigo-600 hover:text-indigo-800 underline"
                target="_blank"
              >
                www.spo.go.kr
              </a>
            </li>
            <li>
              <strong>경찰청 사이버안전국:</strong> 182{' '}
              <a
                href="https://ecrm.cyber.go.kr/"
                className="text-indigo-600 hover:text-indigo-800 underline"
                target="_blank"
              >
                ecrm.cyber.go.kr
              </a>
            </li>
          </ul>
        </section>

        <section className="mb-8">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제14조 위치 정보의 처리
          </h2>
          <p className="text-sm pl-3">
            스팟잇은 위치정보의 보호 및 이용 등에 관한 법률에 따라 이용자의
            개인위치정보를 안전하게 관리합니다.
          </p>

          <h3 className="text-sm font-semibold pl-3 mt-3">
            1. 처리목적 및 보유기간
          </h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>
              스팟잇은 위치정보를 이용자의 현재 위치를 기준으로 주변 가까운
              팝업스토어 정보를 제공하기 위해서만 이용합니다.
            </li>
            <li>
              위치정보는 시스템에 저장되지 않으며 서비스 이용 중에만 처리됩니다.
            </li>
          </ul>

          <h3 className="text-sm font-semibold pl-3 mt-3">
            2. 수집·이용·제공사실 확인자료 보관
          </h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>
              위치정보의 보호 및 이용 등에 관한 법률 제16조 제2항에 따라,
              이용자의 위치정보 수집·이용·제공 사실 확인자료는 위치정보시스템에
              자동으로 기록되며, 6개월 이상 보관합니다.
            </li>
          </ul>

          <h3 className="text-sm font-semibold pl-3 mt-3">
            3. 파기 절차 및 방법
          </h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>
              개인위치정보는 저장되지 않으며, 재생이 불가능한 방법으로 안전하게
              파기합니다.
            </li>
          </ul>

          <h3 className="text-sm font-semibold pl-3 mt-3">
            4. 제3자 제공 및 통보
          </h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>스팟잇은 개인위치정보를 제3자에게 제공하지 않습니다.</li>
          </ul>

          <h3 className="text-sm font-semibold pl-3 mt-3">
            5. 위치정보관리책임자
          </h3>
          <ul className="list-disc list-inside space-y-2 flex flex-col gap-y-1 text-sm pl-6">
            <li>
              스팟잇은 위치정보 보호를 위해 책임자를 지정하고 있으며, 위치정보
              관리책임자는 개인정보 보호 관련 업무를 겸임합니다.
            </li>
          </ul>
        </section>

        <section className="mb-6">
          <h2 className="text-base font-medium text-gray-900 mb-2 border-l-4 border-main pl-3">
            제15조 개인정보 처리방침의 변경
          </h2>
          <p className="text-sm pl-3">
            본 개인정보처리방침은 시행일로부터 적용되며, 법령 및 방침에 따른
            변경내용의 추가, 삭제 및 정정이 있는 경우에는 정정이 있는 경우에는
            개정 최소 7일 전부터 팝가 웹사이트(
            <a
              href="https://www.spotit.co.kr/"
              className="text-indigo-600 hover:text-indigo-800 underline"
              target="_blank"
            >
              https://www.spotit.co.kr/
            </a>
            ) 또는 서비스 내 공지사항을 통해 공지할 것입니다.
          </p>
          <p className="text-sm pl-3 mt-2">
            다만, 이용자 권리의 중대한 변경이 발생할 때에는 최소 30일 전에
            공지하며 필요 시 이용자의 동의를 다시 받을 수도 있습니다.
          </p>

          <div className="text-sm pl-3 mt-4 space-y-1 text-gray-800 font-medium">
            <p>
              <strong>개인정보처리방침 버전:</strong> v 1.0
            </p>
            <p>
              <strong>공고 일자:</strong> 2025년 10월 18일
            </p>
            <p>
              <strong>시행 일자:</strong> 2025년 10월 25일
            </p>
          </div>
        </section>
      </div>
    </div>
  );
}
