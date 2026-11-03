// ffff00009077f4bc  size=48  FUN_ffff00009077f4bc

byte * FUN_ffff00009077f4bc(byte *param_1,uint param_2)

{
  byte *pbVar1;
  byte *pbVar2;
  uint uVar3;
  
  uVar3 = (uint)*param_1;
  pbVar2 = param_1;
  if (param_2 != 0) {
    pbVar2 = (byte *)0x0;
  }
  pbVar1 = pbVar2;
  if (*param_1 != 0) {
    do {
      pbVar2 = param_1;
      if (uVar3 != param_2) {
        pbVar2 = pbVar1;
      }
      param_1 = param_1 + 1;
      uVar3 = (uint)*param_1;
      pbVar1 = pbVar2;
    } while (uVar3 != 0);
  }
  return pbVar2;
}


