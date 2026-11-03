
void FUN_ffff0000be0997bc(void)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    FUN_ffff0000be01a7c8(&LAB_ffff0000be0f45d0);
    return;
  }
  if ((DAT_ffff0000be1b7e30 & 1) == 0) {
    return;
  }
  uVar2 = daif;
  daif = uVar2 & 0xffffffffffffff7f;
  return;
}


