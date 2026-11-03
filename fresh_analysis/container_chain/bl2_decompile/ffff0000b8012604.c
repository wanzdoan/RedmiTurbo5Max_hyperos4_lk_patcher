
void FUN_ffff0000b8012604(void)

{
  bool bVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  
  FUN_ffff0000b801b93c(6,DAT_ffff0000b811af00);
  if (0 < DAT_ffff0000b811af04) {
    uVar3 = 0;
    do {
      FUN_ffff0000b802c208();
      FUN_ffff0000b8051d14(uVar3 & 1);
      iVar4 = 0;
      do {
        FUN_ffff0000b804b704(DAT_ffff0000b811af08 * 1000);
        FUN_ffff0000b80121fc();
        iVar2 = FUN_ffff0000b8020094(0);
        if (iVar2 != 0) {
          FUN_ffff0000b8020158(0);
        }
        bVar1 = iVar4 == 0;
        iVar4 = iVar4 + 1;
      } while (bVar1 && iVar2 == 0);
      uVar3 = uVar3 + 1;
    } while (((int)uVar3 < DAT_ffff0000b811af04) && (iVar2 == 0));
  }
  FUN_ffff0000b801ba64();
  FUN_ffff0000b80520cc(0);
  FUN_ffff0000b8012a34();
  return;
}


