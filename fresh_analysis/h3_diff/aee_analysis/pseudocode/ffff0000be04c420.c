
void FUN_ffff0000be04c420(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  lVar1 = FUN_ffff0000be05f1bc(0x400);
  if (lVar1 != 0) {
    FUN_ffff0000be0607b0(lVar1,0xffff00000c2cd450,0x400);
    uVar2 = FUN_ffff0000be05d228(lVar1);
    uVar3 = (*param_1)(uVar2,0x400);
    if ((uVar3 & 1) == 0) {
      FUN_ffff0000be05fd98(s__thermaldrv__log_dump_fail_ffff0000be0aecdc);
    }
    thunk_FUN_ffff0000be09937c(lVar1);
    return;
  }
  FUN_ffff0000be05fd98(s__thermaldrv__malloc_failed_ffff0000be0a740e);
  return;
}


