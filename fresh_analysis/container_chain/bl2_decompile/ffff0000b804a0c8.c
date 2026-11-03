
void FUN_ffff0000b804a0c8(void)

{
  undefined *puVar1;
  undefined *puVar2;
  undefined **ppuVar3;
  
  FUN_ffff0000b804a1d0();
  puVar1 = PTR_DAT_ffff0000b81086d8;
  if (DAT_ffff0000b80ad010 != (undefined *)0x0) {
    ppuVar3 = &PTR_DAT_ffff0000b80ad020;
    puVar2 = DAT_ffff0000b80ad010;
    do {
      if (((ulong)ppuVar3[-1] & 1) == 0) {
        FUN_ffff0000b804a1f8
                  (puVar1,*ppuVar3,(ulong)(puVar2 + 0xfffff) & 0xfffffffffff00000,
                   (ulong)ppuVar3[-3] & 0xfffffffffff00000);
      }
      puVar2 = ppuVar3[3];
      ppuVar3 = ppuVar3 + 5;
    } while (puVar2 != (undefined *)0x0);
  }
  return;
}


