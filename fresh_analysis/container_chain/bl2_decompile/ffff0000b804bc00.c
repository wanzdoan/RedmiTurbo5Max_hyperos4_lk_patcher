
void FUN_ffff0000b804bc00(void)

{
  undefined8 *puVar1;
  long *plVar2;
  undefined8 uVar3;
  ulong uVar4;
  undefined *puVar5;
  undefined1 **ppuVar6;
  undefined1 **ppuVar7;
  undefined1 *local_30;
  long *local_28;
  
  local_28 = (long *)&local_30;
  local_30 = (undefined1 *)&local_30;
  uVar3 = daif;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    daif = uVar4 | 0x80;
  }
  ppuVar6 = &local_30;
  ppuVar7 = &local_30;
  if ((undefined **)PTR_LOOP_ffff0000b8102108 != &PTR_LOOP_ffff0000b8102100) {
    do {
      local_28 = (long *)ppuVar7;
      local_30 = (undefined1 *)ppuVar6;
      puVar5 = PTR_LOOP_ffff0000b8102108;
      puVar1 = *(undefined8 **)(PTR_LOOP_ffff0000b8102108 + 8);
      *puVar1 = *(undefined8 *)PTR_LOOP_ffff0000b8102108;
      *(undefined8 **)(*(long *)puVar5 + 8) = puVar1;
      *(undefined8 *)(puVar5 + 8) = 0;
      *(undefined1 ***)puVar5 = &local_30;
      *(long **)(puVar5 + 8) = local_28;
      *local_28 = (long)puVar5;
      local_28 = (long *)puVar5;
      ppuVar6 = (undefined1 **)local_30;
      ppuVar7 = (undefined1 **)local_28;
    } while ((undefined **)PTR_LOOP_ffff0000b8102108 != &PTR_LOOP_ffff0000b8102100);
  }
  DAT_ffff0000b8129c50 = 0;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    daif = uVar4 & 0xffffffffffffff7f;
  }
  if ((undefined1 **)local_28 != &local_30) {
    do {
      plVar2 = (long *)local_28[1];
      *plVar2 = *local_28;
      *(long **)(*local_28 + 8) = plVar2;
      *local_28 = 0;
      local_28[1] = 0;
      FUN_ffff0000b8082558();
    } while ((undefined1 **)local_28 != &local_30);
  }
  return;
}


