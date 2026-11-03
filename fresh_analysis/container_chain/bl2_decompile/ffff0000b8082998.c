
void FUN_ffff0000b8082998(void)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    FUN_ffff0000b800ae28(&LAB_ffff0000b8107d68);
    return;
  }
  if ((DAT_ffff0000b81b9b50 & 1) == 0) {
    return;
  }
  uVar2 = daif;
  daif = uVar2 & 0xffffffffffffff7f;
  return;
}


