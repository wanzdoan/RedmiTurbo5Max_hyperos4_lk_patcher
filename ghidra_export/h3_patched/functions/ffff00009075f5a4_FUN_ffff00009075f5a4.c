// ffff00009075f5a4  size=132  FUN_ffff00009075f5a4

void FUN_ffff00009075f5a4(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff00009077d24c(0xc00);
  if (lVar1 != 0) {
    FUN_ffff00009077e858(lVar1,0xffff00000c2c5050,0xc00);
    uVar2 = FUN_ffff00009077b06c(lVar1);
    uVar3 = (*param_1)(uVar2,0xc00);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077de54(s__thermaldrv__cpu_cooler_log_dump_ffff0000907e67cd);
    }
    thunk_FUN_ffff0000907c2ad8(lVar1);
    return;
  }
  FUN_ffff00009077de54(s__thermaldrv__cpu_cooler_malloc_f_ffff0000907d8114);
  return;
}


