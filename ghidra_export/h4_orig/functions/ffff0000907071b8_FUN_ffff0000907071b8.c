// ffff0000907071b8  size=128  FUN_ffff0000907071b8

void FUN_ffff0000907071b8(void)

{
  int iVar1;
  ulong uVar2;
  undefined *puVar3;
  ulong uVar4;
  undefined **ppuVar5;
  
  iVar1 = FUN_ffff0000907414e4();
  if (iVar1 != 0) {
    uVar4 = 0;
    ppuVar5 = &PTR_FUN_ffff000090816a50;
    puVar3 = &DAT_ffff000090816928;
    do {
      iVar1 = (&DAT_ffff000090803dd0)[uVar4];
      *(undefined **)(puVar3 + 0x20) = puVar3 + -0x40;
      *(long *)(puVar3 + 0x10) = (long)&DAT_ffff000090803dd0 + (long)iVar1;
      *(undefined ***)(puVar3 + 0x18) = ppuVar5;
      FUN_ffff00009071ee80(puVar3);
      uVar4 = uVar4 + 1;
      uVar2 = FUN_ffff0000907414e4();
      ppuVar5 = ppuVar5 + 9;
      puVar3 = puVar3 + 0xb8;
    } while (uVar4 < (uVar2 & 0xffffffff));
  }
  return;
}


