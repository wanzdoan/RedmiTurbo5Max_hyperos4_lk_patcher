
/* RtcSbpController::getSbpFromFile() */

int RtcSbpController::getSbpFromFile(void)

{
  long lVar1;
  int __fd;
  int iVar2;
  uint uVar3;
  ulong uVar4;
  ssize_t sVar5;
  char local_48 [16];
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_48[0] = '\0';
  local_48[1] = '\0';
  local_48[2] = '\0';
  local_48[3] = '\0';
  local_48[4] = '\0';
  local_48[5] = '\0';
  local_48[6] = '\0';
  local_48[7] = '\0';
  local_48[8] = '\0';
  local_48[9] = '\0';
  local_48[10] = '\0';
  local_48[0xb] = '\0';
  local_48[0xc] = '\0';
  local_48[0xd] = '\0';
  local_48[0xe] = '\0';
  local_48[0xf] = '\0';
  uVar4 = __open_2("/mnt/vendor/nvdata/APCFG/APRDCL/CXP_SBP",0);
  __fd = (int)uVar4;
  if (__fd < 0) {
    iVar2 = -1;
  }
  else {
    sVar5 = read(__fd,local_48,0xf);
    if ((int)sVar5 < 1) {
      iVar2 = -1;
    }
    else {
      iVar2 = atoi(local_48);
    }
    uVar3 = close(__fd);
    uVar4 = (ulong)uVar3;
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return iVar2;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar4);
}


