// File offset entry: 0x91d50

bool FUN_02091c50(uint param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  
  uVar2 = FUN_02091b28();
  uVar1 = -param_1;
  if (uVar2 < param_1) {
    uVar1 = ~param_1;
  }
  return param_2 <= uVar1 + uVar2;
}


