// ffff00009073fc74  size=88  FUN_ffff00009073fc74

void FUN_ffff00009073fc74(code *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = FUN_ffff00009077b334(0xffff00001c005000);
  uVar2 = (*param_1)(uVar1,0x8000);
  if ((uVar2 & 1) != 0) {
    return;
  }
  FUN_ffff00009077e11c(s__SSPM__AEE_EXPDB_SPM_SRAM_DUMP_f_ffff0000907e1771);
  return;
}


