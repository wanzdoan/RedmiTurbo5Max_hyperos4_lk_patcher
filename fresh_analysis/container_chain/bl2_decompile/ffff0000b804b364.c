
void FUN_ffff0000b804b364(undefined8 *param_1)

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
  FUN_ffff0000b800ad90(&DAT_ffff0000b81020c8,0xffffffff);
  *param_1 = PTR_LOOP_ffff0000b81020b8;
  param_1[1] = &PTR_LOOP_ffff0000b81020b8;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000b81020b8 + 8);
  PTR_LOOP_ffff0000b81020b8 = (undefined *)param_1;
  *puVar1 = param_1;
  FUN_ffff0000b800ae28(&DAT_ffff0000b81020c8);
  return;
}


