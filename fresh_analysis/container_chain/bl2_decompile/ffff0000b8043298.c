
void FUN_ffff0000b8043298(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff0000b804bccc(0x400);
  if (lVar1 != 0) {
    FUN_ffff0000b804cd40(lVar1,0xffff00000c2cd450,0x400);
    uVar2 = FUN_ffff0000b8049ec8(lVar1);
    uVar3 = (*param_1)(uVar2,0x400);
    if ((uVar3 & 1) == 0) {
      FUN_ffff0000b804c33c(s__thermaldrv__log_dump_fail_ffff0000b808e3f4);
    }
    thunk_FUN_ffff0000b8082558(lVar1);
    return;
  }
  FUN_ffff0000b804c33c(s__thermaldrv__malloc_failed_ffff0000b808873e);
  return;
}


