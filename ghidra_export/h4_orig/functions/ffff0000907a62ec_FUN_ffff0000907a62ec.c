// ffff0000907a62ec  size=172  FUN_ffff0000907a62ec

void FUN_ffff0000907a62ec
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined8 param_5)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  iVar1 = FUN_ffff00009071c6e8();
  uVar2 = param_3;
  uVar3 = param_1;
  uVar4 = param_2;
  if ((iVar1 != 2) && (uVar2 = param_1, uVar3 = param_2, uVar4 = param_3, iVar1 == 1)) {
    thunk_FUN_ffff0000907ab288(param_1,param_2,param_3,param_4,param_5);
    return;
  }
  FUN_ffff0000907a6da4(uVar2,uVar3,uVar4,param_4,param_5,1);
  return;
}


