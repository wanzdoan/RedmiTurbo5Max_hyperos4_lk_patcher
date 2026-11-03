
void FUN_ffff0000b804331c(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff0000b804bccc(0xc00);
  if (lVar1 != 0) {
    FUN_ffff0000b804cd40(lVar1,0xffff00000c2c5050,0xc00);
    uVar2 = FUN_ffff0000b8049ec8(lVar1);
    uVar3 = (*param_1)(uVar2,0xc00);
    if ((uVar3 & 1) == 0) {
      FUN_ffff0000b804c33c(s__thermaldrv__cpu_cooler_log_dump_ffff0000b8091d1a);
    }
    thunk_FUN_ffff0000b8082558(lVar1);
    return;
  }
  FUN_ffff0000b804c33c(s__thermaldrv__cpu_cooler_malloc_f_ffff0000b808a1bd);
  return;
}


