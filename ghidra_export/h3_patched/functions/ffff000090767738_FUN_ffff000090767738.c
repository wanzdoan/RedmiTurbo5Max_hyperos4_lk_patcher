// ffff000090767738  size=168  FUN_ffff000090767738

void FUN_ffff000090767738(void)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  undefined8 local_38;
  undefined5 uStack_30;
  undefined3 local_2b;
  undefined5 uStack_28;
  
  uStack_28 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_2b = 0;
  lVar2 = FUN_ffff000090719218();
  lVar3 = FUN_ffff00009071922c();
  uVar1 = *(int *)(lVar2 + 0x4c) + *(int *)(lVar2 + 0x48) + *(int *)(lVar2 + 0x58);
  FUN_ffff00009077eb88(&local_38,0,0x15);
  if (0x13 < uVar1) {
    uVar4 = (ulong)uVar1 / 0x14;
    do {
      FUN_ffff00009077e858(&local_38,lVar3,0x14);
      FUN_ffff000090709c60(&local_38);
      lVar3 = lVar3 + 0x14;
      uVar4 = uVar4 - 1;
    } while (uVar4 != 0);
  }
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


