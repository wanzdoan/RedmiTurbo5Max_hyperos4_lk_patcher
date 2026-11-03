
undefined4 FUN_ffff0000be047c70(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  
  uVar3 = FUN_ffff0000be063c7c();
  uVar2 = DAT_ffff0000be126078;
  if (((uVar3 & 1) != 0) &&
     (iVar1 = FUN_ffff0000be065774(), uVar2 = DAT_ffff0000be126078, iVar1 == 0xfc11)) {
    uVar2 = 0;
  }
  return uVar2;
}


