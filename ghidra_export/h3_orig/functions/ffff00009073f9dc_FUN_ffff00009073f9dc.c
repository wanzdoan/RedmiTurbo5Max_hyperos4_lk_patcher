// ffff00009073f9dc  size=88  FUN_ffff00009073f9dc

void FUN_ffff00009073f9dc(code *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = FUN_ffff00009077b06c(0xffff00001c005000);
  uVar2 = (*param_1)(uVar1,0x8000);
  if ((uVar2 & 1) != 0) {
    return;
  }
  FUN_ffff00009077de54(s__SSPM__AEE_EXPDB_SPM_SRAM_DUMP_f_ffff0000907e16a0);
  return;
}


