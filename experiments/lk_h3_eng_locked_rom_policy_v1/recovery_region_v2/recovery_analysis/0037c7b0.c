
/* get_package_sha_map(char*, MemMapping*) */

void get_package_sha_map(char *param_1,MemMapping *param_2)

{
  undefined4 uVar1;
  long lVar2;
  uchar *md;
  undefined8 uVar3;
  ulong uVar4;
  undefined4 *puVar5;
  ostream *poVar6;
  char *pcVar7;
  long lVar8;
  LogMessage aLStack_50 [8];
  long local_48;

  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  puts("Get package\'s sha bay MemMapping.");
  md = malloc(0x14);
  if (md == (uchar *)0x0) {
    uVar4 = android::base::ShouldLog(4,0);
    if ((uVar4 & 1) == 0) {
      uVar3 = 0xffffffff;
    }
    else {
      puVar5 = (undefined4 *)__errno();
      uVar1 = *puVar5;
      android::base::LogMessage::LogMessage
                (aLStack_50,"bootable/recovery/miutil/mi_verification.cpp",0xe5,4,0,0xffffffff);
      poVar6 = (ostream *)android::base::LogMessage::stream(aLStack_50);
      poVar6 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         (poVar6,"get_package_sha_map failed to allocate",0x26);
      poVar6 = (ostream *)std::ostream::operator<<(poVar6,0x14);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar6," bytes",6);
      android::base::LogMessage::~LogMessage(aLStack_50);
      uVar3 = 0xffffffff;
      *puVar5 = uVar1;
    }
  }
  else {
    SHA1(*(uchar **)param_2,*(size_t *)(param_2 + 8),md);
    lVar8 = 0;
    pcVar7 = param_1;
    do {
      sprintf(pcVar7,(char *)0xffffffffffffffff);
      lVar8 = lVar8 + 1;
      pcVar7 = pcVar7 + 2;
    } while (lVar8 != 0x14);
    param_1[0x28] = '\0';
    free(md);
    uVar3 = 0;
  }
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar3);
}
