// ffff00009072795c  size=192  FUN_ffff00009072795c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072795c(void)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  undefined4 *puVar4;
  
  puVar4 = (undefined4 *)&DAT_ffff00004b800068;
  _DAT_ffff00004b800074 = 1;
  if ((_DAT_ffff00004b800078 & 1) == 0) {
LAB_ffff00009072798c:
    _DAT_ffff00004b800060 = 1;
    if ((_DAT_ffff00004b800068 & 1) != 0) {
      _DAT_ffff00004b800060 = 1;
      return;
    }
    lVar1 = FUN_ffff00009071676c();
    do {
      uVar2 = FUN_ffff00009071676c();
      if ((_DAT_ffff00004b800068 & 1) != 0) {
        return;
      }
    } while (uVar2 < lVar1 + 5U);
    pcVar3 = s__s__MFG_RPC_SLV_CTRL_UPDATE_no_a_ffff0000907da81c;
  }
  else {
    lVar1 = FUN_ffff00009071676c();
    do {
      uVar2 = FUN_ffff00009071676c();
      if ((_DAT_ffff00004b800078 & 1) == 0) goto LAB_ffff00009072798c;
    } while (uVar2 < lVar1 + 5U);
    puVar4 = (undefined4 *)&DAT_ffff00004b800078;
    pcVar3 = s__s__MFG_RPC_SLV_SLP_PROT_RDY_STA_ffff0000907fecca;
  }
  FUN_ffff00009077de54(pcVar3,s__GPUEB__EXPDB__ffff0000907fb40d,*puVar4);
  return;
}


