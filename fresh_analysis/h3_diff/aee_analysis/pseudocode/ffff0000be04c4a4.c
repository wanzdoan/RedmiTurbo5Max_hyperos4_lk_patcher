
void FUN_ffff0000be04c4a4(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff0000be05f1bc(0xc00);
  if (lVar1 != 0) {
    FUN_ffff0000be0607b0(lVar1,0xffff00000c2c5050,0xc00);
    uVar2 = FUN_ffff0000be05d228(lVar1);
    uVar3 = (*param_1)(uVar2,0xc00);
    if ((uVar3 & 1) == 0) {
      FUN_ffff0000be05fd98(s__thermaldrv__cpu_cooler_log_dump_ffff0000be0b33b0);
    }
    thunk_FUN_ffff0000be09937c(lVar1);
    return;
  }
  FUN_ffff0000be05fd98(s__thermaldrv__cpu_cooler_malloc_f_ffff0000be0a9678);
  return;
}


