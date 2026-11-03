
void FUN_ffff0000be06aa9c(ulong param_1,undefined4 *param_2,uint param_3)

{
  long lVar1;
  uint uVar2;
  ulong uVar3;
  undefined4 *puVar4;
  undefined4 local_14;
  
  lVar1 = (param_1 & 0xffffffff) * 0x10;
  if ((param_3 & 0xffff) < 4) {
    uVar3 = 0;
  }
  else {
    uVar2 = (param_3 & 0xffff) >> 2;
    puVar4 = param_2;
    do {
      uVar2 = uVar2 - 1;
      *puVar4 = *(undefined4 *)(lVar1 + -0xffffe98fed00);
      puVar4 = puVar4 + 1;
    } while (uVar2 != 0);
    uVar3 = (ulong)(param_3 & 0xfffc);
  }
  if ((param_3 & 3) != 0) {
    local_14 = *(undefined4 *)(lVar1 + -0xffffe98fed00);
    FUN_ffff0000be0607b0((long)param_2 + uVar3,&local_14,param_3 & 3);
  }
  return;
}


