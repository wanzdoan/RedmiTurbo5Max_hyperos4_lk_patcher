
void FUN_ffff0000be02fda8(code *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = FUN_ffff0000be05d228(0xffff00001c005000);
  uVar2 = (*param_1)(uVar1,0x8000);
  if ((uVar2 & 1) != 0) {
    return;
  }
  FUN_ffff0000be05fd98(s__SSPM__AEE_EXPDB_SPM_SRAM_DUMP_f_ffff0000be0afc9a);
  return;
}


