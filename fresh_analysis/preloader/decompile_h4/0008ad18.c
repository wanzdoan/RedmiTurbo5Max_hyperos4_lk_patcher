// File offset entry: 0x8ad18

void FUN_0208ac18(int param_1)

{
  int iVar1;
  undefined1 *puVar2;
  undefined1 *puVar3;
  
  uRam000000000211d9f0 = (uint)(param_1 == 0);
  puVar3 = puRam000000000211da00;
  if (param_1 != 0) {
    puVar2 = puRam000000000211d9f8;
    for (iVar1 = (int)puRam000000000211d9f8 - (int)puRam000000000211da00;
        puVar3 = puRam000000000211d9f8, iVar1 != 0; iVar1 = iVar1 + 1) {
      FUN_0208ac88(*puVar2);
      puVar2 = puVar2 + 1;
    }
  }
  puRam000000000211da00 = puVar3;
  return;
}


