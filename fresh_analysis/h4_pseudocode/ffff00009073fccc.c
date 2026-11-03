
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073fccc(code *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  
  _DAT_ffff00001c00d100 = _DAT_ffff00001c00d100 | 2;
  uVar1 = FUN_ffff00009077b334(0xffff00001c00f000);
  uVar2 = (*param_1)(uVar1,0x1000);
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077e11c(s__SPM__AEE_EXPDB_SPM_OCLA_SRAM_DU_ffff0000907fd6e3);
  }
  _DAT_ffff00001c00d100 = _DAT_ffff00001c00d100 & 0xfffffffd;
  return;
}


