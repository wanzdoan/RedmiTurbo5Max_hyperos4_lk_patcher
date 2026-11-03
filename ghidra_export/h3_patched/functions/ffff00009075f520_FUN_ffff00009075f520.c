// ffff00009075f520  size=132  FUN_ffff00009075f520

void FUN_ffff00009075f520(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff00009077d24c(0x400);
  if (lVar1 != 0) {
    FUN_ffff00009077e858(lVar1,0xffff00000c2cd450,0x400);
    uVar2 = FUN_ffff00009077b06c(lVar1);
    uVar3 = (*param_1)(uVar2,0x400);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077de54(s__thermaldrv__log_dump_fail_ffff0000907dffb0);
    }
    thunk_FUN_ffff0000907c2ad8(lVar1);
    return;
  }
  FUN_ffff00009077de54(s__thermaldrv__malloc_failed_ffff0000907d4d33);
  return;
}


