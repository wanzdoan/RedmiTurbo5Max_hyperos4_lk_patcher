
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* HasCache() */

void HasCache(void)

{
  long lVar1;
  int iVar2;
  ostream *poVar3;
  long lVar4;
  string *in_x2;
  LogMessage local_40 [16];
  void *local_30;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  if (DAT_0057bcf0 == DAT_0057bcf8) {
    android::base::LogMessage::LogMessage
              (local_40,"bootable/recovery/recovery_utils/roots.cpp",0x244,6,0,0xffffffff);
    poVar3 = (ostream *)android::base::LogMessage::stream(local_40);
    poVar3 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                       (poVar3,"Check failed: !fstab.empty()",0x1c);
    in_x2 = (string *)0x1;
    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar3," ",1);
    android::base::LogMessage::~LogMessage(local_40);
  }
  if (((DAT_0057bd88 & 1) == 0) && (iVar2 = __cxa_guard_acquire(&DAT_0057bd88), iVar2 != 0)) {
    _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
              (local_40,"/cache");
    lVar4 = android::fs_mgr::GetEntryForMountPoint((fs_mgr *)&DAT_0057bcf0,(vector *)local_40,in_x2)
    ;
    if (((byte)local_40[0] & 1) != 0) {
      operator_delete(local_30);
    }
    DAT_0057bd80 = lVar4 != 0;
    __cxa_guard_release(&DAT_0057bd88);
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(DAT_0057bd80 != '\0');
}
