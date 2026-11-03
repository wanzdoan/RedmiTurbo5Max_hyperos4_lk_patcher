
void FUN_ffff00009077c9f0(undefined8 *param_1)

{
  undefined8 *puVar1;
  int *piVar2;
  char cVar3;
  bool bVar4;
  
  piVar2 = (int *)(param_1 + 2);
  do {
    cVar3 = '\x01';
    bVar4 = (bool)ExclusiveMonitorPass(piVar2,0x10);
    if (bVar4) {
      *piVar2 = *piVar2 + 1;
      cVar3 = ExclusiveMonitorsStatus();
    }
  } while (cVar3 != '\0');
  FUN_ffff000090717738(&DAT_ffff0000908328f0,0xffffffff);
  *param_1 = PTR_LOOP_ffff0000908328e0;
  param_1[1] = &PTR_LOOP_ffff0000908328e0;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000908328e0 + 8);
  PTR_LOOP_ffff0000908328e0 = (undefined *)param_1;
  *puVar1 = param_1;
  FUN_ffff0000907177d0(&DAT_ffff0000908328f0);
  return;
}


