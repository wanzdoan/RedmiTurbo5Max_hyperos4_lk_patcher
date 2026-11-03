// ffff00009075f83c  size=132  FUN_ffff00009075f83c

void FUN_ffff00009075f83c(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff00009077d514(0xc00);
  if (lVar1 != 0) {
    FUN_ffff00009077eb20(lVar1,0xffff00000c2c5050,0xc00);
    uVar2 = FUN_ffff00009077b334(lVar1);
    uVar3 = (*param_1)(uVar2,0xc00);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077e11c(s__thermaldrv__cpu_cooler_log_dump_ffff0000907e68f8);
    }
    thunk_FUN_ffff0000907c314c(lVar1);
    return;
  }
  FUN_ffff00009077e11c(s__thermaldrv__cpu_cooler_malloc_f_ffff0000907d81aa);
  return;
}


