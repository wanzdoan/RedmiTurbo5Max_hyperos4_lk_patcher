
undefined4 FUN_ffff0000b803e8ac(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff0000b804f554();
  uVar2 = DAT_ffff0000b8129878;
  if (((uVar3 & 1) != 0) &&
     (iVar1 = FUN_ffff0000b8050384(), uVar2 = DAT_ffff0000b8129878, iVar1 == 0xfc11)) {
    uVar2 = 0;
  }
  return uVar2;
}


