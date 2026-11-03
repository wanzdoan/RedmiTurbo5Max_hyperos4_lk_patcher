
void FUN_ffff0000be01a4fc(undefined4 *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined8 *puVar3;
  
  puVar3 = DAT_ffff0000be0f5518;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    *param_1 = 0;
    *(undefined1 *)(param_1 + 1) = 0;
    param_1[2] = 0;
    *puVar3 = 1;
    FUN_ffff0000be01b658(param_1 + 4,1);
    *puVar3 = 0;
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  else {
    *param_1 = 0;
    *(undefined1 *)(param_1 + 1) = 0;
    param_1[2] = 0;
    *puVar3 = 1;
    FUN_ffff0000be01b658(param_1 + 4,1);
    *puVar3 = 0;
  }
  return;
}


