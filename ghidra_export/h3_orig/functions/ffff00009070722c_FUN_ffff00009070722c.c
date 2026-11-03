// ffff00009070722c  size=128  FUN_ffff00009070722c

void FUN_ffff00009070722c(void)

{
  int iVar1;
  ulong uVar2;
  undefined *puVar3;
  ulong uVar4;
  undefined **ppuVar5;
  
  iVar1 = FUN_ffff00009074124c();
  if (iVar1 != 0) {
    uVar4 = 0;
    ppuVar5 = &PTR_FUN_ffff000090816a40;
    puVar3 = &DAT_ffff000090816918;
    do {
      iVar1 = (&DAT_ffff000090803ba8)[uVar4];
      *(undefined **)(puVar3 + 0x20) = puVar3 + -0x40;
      *(long *)(puVar3 + 0x10) = (long)&DAT_ffff000090803ba8 + (long)iVar1;
      *(undefined ***)(puVar3 + 0x18) = ppuVar5;
      FUN_ffff00009071ecf8(puVar3);
      uVar4 = uVar4 + 1;
      uVar2 = FUN_ffff00009074124c();
      ppuVar5 = ppuVar5 + 9;
      puVar3 = puVar3 + 0xb8;
    } while (uVar4 < (uVar2 & 0xffffffff));
  }
  return;
}


