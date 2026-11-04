
/* Package::CreateMemoryPackage(std::string const&, std::function<void (float)> const&) */

void __thiscall Package::CreateMemoryPackage(Package *this,string *param_1,function *param_2)

{
  undefined4 uVar1;
  long lVar2;
  MemMapping *pMVar3;
  ulong uVar4;
  MemoryPackage *pMVar5;
  undefined4 *puVar6;
  ostream *poVar7;
  undefined8 *in_x8;
  MemMapping *local_50;
  long local_48;

  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  pMVar3 = operator_new(0x28);
  *(undefined8 *)(pMVar3 + 0x20) = 0;
  *(undefined8 *)(pMVar3 + 8) = 0;
  *(undefined8 *)pMVar3 = 0;
  *(undefined8 *)(pMVar3 + 0x18) = 0;
  *(undefined8 *)(pMVar3 + 0x10) = 0;
  uVar4 = MemMapping::MapFile(pMVar3,(string *)this);
  if ((uVar4 & 1) == 0) {
    uVar4 = android::base::ShouldLog(4,0);
    if ((uVar4 & 1) != 0) {
      puVar6 = (undefined4 *)__errno();
      uVar1 = *puVar6;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_50,"bootable/recovery/install/package.cpp",0x7f,4,0,0xffffffff
                );
      poVar7 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_50);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar7,"failed to map file",0x12);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_50);
      *puVar6 = uVar1;
    }
    *in_x8 = 0;
    MemMapping::~MemMapping(pMVar3);
    operator_delete(pMVar3);
  }
  else {
    pMVar5 = operator_new(0x90);
    local_50 = pMVar3;
    MemoryPackage::MemoryPackage(pMVar5,this,&local_50,param_1);
    pMVar3 = local_50;
    local_50 = (MemMapping *)0x0;
    if (pMVar3 != (MemMapping *)0x0) {
      MemMapping::~MemMapping(pMVar3);
      operator_delete(pMVar3);
    }
    *in_x8 = pMVar5;
  }
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
