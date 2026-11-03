
void FUN_ffff00009077b534(void)

{
  undefined *puVar1;
  undefined *puVar2;
  undefined **ppuVar3;
  
  FUN_ffff00009077b63c();
  puVar1 = PTR_DAT_ffff000090839928;
  if (DAT_ffff000090816010 != (undefined *)0x0) {
    ppuVar3 = &PTR_DAT_ffff000090816020;
    puVar2 = DAT_ffff000090816010;
    do {
      if (((ulong)ppuVar3[-1] & 1) == 0) {
        FUN_ffff00009077b664
                  (puVar1,*ppuVar3,(ulong)(puVar2 + 0xfffff) & 0xfffffffffff00000,
                   (ulong)ppuVar3[-3] & 0xfffffffffff00000);
      }
      puVar2 = ppuVar3[3];
      ppuVar3 = ppuVar3 + 5;
    } while (puVar2 != (undefined *)0x0);
  }
  return;
}


