
void FUN_ffff0000907c358c(void)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    FUN_ffff0000907177d0(&LAB_ffff0000908384a0);
    return;
  }
  if ((DAT_ffff00009090d960 & 1) == 0) {
    return;
  }
  uVar2 = daif;
  daif = uVar2 & 0xffffffffffffff7f;
  return;
}


