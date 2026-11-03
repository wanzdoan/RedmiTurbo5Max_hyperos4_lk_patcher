// ffff00009077ca60  size=172  FUN_ffff00009077ca60

void FUN_ffff00009077ca60(long *param_1)

{
  long *plVar1;
  char cVar2;
  bool bVar3;
  int iVar4;
  
  FUN_ffff000090717738(&DAT_ffff0000908328f0,0xffffffff);
  plVar1 = (long *)param_1[1];
  *plVar1 = *param_1;
  *(long **)(*param_1 + 8) = plVar1;
  *param_1 = 0;
  param_1[1] = 0;
  FUN_ffff0000907177d0(&DAT_ffff0000908328f0);
  plVar1 = param_1 + 2;
  do {
    iVar4 = (int)*plVar1 + -1;
    cVar2 = '\x01';
    bVar3 = (bool)ExclusiveMonitorPass(plVar1,0x10);
    if (bVar3) {
      *(int *)plVar1 = iVar4;
      cVar2 = ExclusiveMonitorsStatus();
    }
  } while (cVar2 != '\0');
  if (iVar4 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__last_ref__removing___s__ffff0000907eecfb,s_bdev_dec_ref_ffff0000907e6bf8,
               0x146,param_1[3]);
    if ((code *)param_1[0x14] != (code *)0x0) {
      (*(code *)param_1[0x14])(param_1);
    }
    thunk_FUN_ffff0000907c314c(param_1[3]);
    return;
  }
  return;
}


