
/* RtcSbpController::saveSbpIdToFile(int) */

void RtcSbpController::saveSbpIdToFile(int param_1)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  uint *puVar5;
  ulong extraout_x1;
  char *pcVar6;
  char local_48 [16];
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_48[8] = 0;
  local_48[0] = '\0';
  local_48[1] = '\0';
  local_48[2] = '\0';
  local_48[3] = '\0';
  local_48[4] = '\0';
  local_48[5] = '\0';
  local_48[6] = '\0';
  local_48[7] = '\0';
  __umask_chk(0x1b);
  pcVar6 = (char *)0x1a4;
  uVar2 = open("/mnt/vendor/nvdata/APCFG/APRDCL/CXP_SBP",0x241);
  __umask_chk(0x3f);
  if ((int)uVar2 < 0) {
    puVar5 = (uint *)__errno();
    if (*(long *)(lVar1 + 0x28) == local_38) {
      RfxController::logE((char *)(ulong)(uint)param_1,"RtcSbp",
                          "[%s]Open NVDATA CXP_SBP fail, fd:%d, error=%d\n","saveSbpIdToFile",
                          (ulong)uVar2,(ulong)*puVar5);
      return;
    }
  }
  else {
    iVar3 = ::snprintf(local_48,extraout_x1,pcVar6);
    uVar4 = __write_chk(uVar2,local_48,(long)iVar3,9);
    puVar5 = (uint *)__errno();
    RfxController::logI((char *)(ulong)(uint)param_1,"RtcSbp",
                        "[%s]fd:%d, write ret value:%d, error=%d\n","saveSbpIdToFile",(ulong)uVar2,
                        uVar4 & 0xffffffff,(ulong)*puVar5);
    uVar2 = close(uVar2);
    puVar5 = (uint *)(ulong)uVar2;
    if (*(long *)(lVar1 + 0x28) == local_38) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(puVar5);
}


