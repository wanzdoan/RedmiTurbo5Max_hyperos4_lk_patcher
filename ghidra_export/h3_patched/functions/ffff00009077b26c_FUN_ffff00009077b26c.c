// ffff00009077b26c  size=132  FUN_ffff00009077b26c

void FUN_ffff00009077b26c(void)

{
  undefined8 uVar1;
  undefined *puVar2;
  undefined **ppuVar3;
  
  FUN_ffff00009077b374();
  uVar1 = DAT_ffff000090839908;
  if (DAT_ffff000090816010 != (undefined *)0x0) {
    ppuVar3 = &PTR_DAT_ffff000090816020;
    puVar2 = DAT_ffff000090816010;
    do {
      if (((ulong)ppuVar3[-1] & 1) == 0) {
        FUN_ffff00009077b39c
                  (uVar1,*ppuVar3,(ulong)(puVar2 + 0xfffff) & 0xfffffffffff00000,
                   (ulong)ppuVar3[-3] & 0xfffffffffff00000);
      }
      puVar2 = ppuVar3[3];
      ppuVar3 = ppuVar3 + 5;
    } while (puVar2 != (undefined *)0x0);
  }
  return;
}


