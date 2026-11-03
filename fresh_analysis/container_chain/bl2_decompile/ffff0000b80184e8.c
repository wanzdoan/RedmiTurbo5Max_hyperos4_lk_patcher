
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b80184e8(void)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  undefined4 *puVar4;
  
  puVar4 = (undefined4 *)&DAT_ffff00004b800068;
  _DAT_ffff00004b800074 = 1;
  if ((_DAT_ffff00004b800078 & 1) == 0) {
LAB_ffff0000b8018518:
    _DAT_ffff00004b800060 = 1;
    if ((_DAT_ffff00004b800068 & 1) != 0) {
      _DAT_ffff00004b800060 = 1;
      return;
    }
    lVar1 = FUN_ffff0000b8009fb4();
    do {
      uVar2 = FUN_ffff0000b8009fb4();
      if ((_DAT_ffff00004b800068 & 1) != 0) {
        return;
      }
    } while (uVar2 < lVar1 + 5U);
    pcVar3 = s__s__MFG_RPC_SLV_CTRL_UPDATE_no_a_ffff0000b808b722;
  }
  else {
    lVar1 = FUN_ffff0000b8009fb4();
    do {
      uVar2 = FUN_ffff0000b8009fb4();
      if ((_DAT_ffff00004b800078 & 1) == 0) goto LAB_ffff0000b8018518;
    } while (uVar2 < lVar1 + 5U);
    puVar4 = (undefined4 *)&DAT_ffff00004b800078;
    pcVar3 = s__s__MFG_RPC_SLV_SLP_PROT_RDY_STA_ffff0000b809f5a7;
  }
  FUN_ffff0000b804c33c(pcVar3,s__GPUEB__EXPDB__ffff0000b809d5e3,*puVar4);
  return;
}


