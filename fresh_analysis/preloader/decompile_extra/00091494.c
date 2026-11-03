// File offset entry: 0x91494

void FUN_02091394(undefined4 param_1,undefined8 param_2,undefined8 param_3,ulong param_4)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  
  param_4 = param_4 & 0xffffffff;
  lVar3 = FUN_020036c8(1);
  if (lVar3 == 0) {
    FUN_02063328(0x300000);
    FUN_0208ada0(0x20b0cbf,0x20cec01,1);
  }
  else {
    uVar1 = FUN_02091294(param_1);
    iVar2 = FUN_02003ac0(lVar3,param_2,param_4,param_3,uVar1);
    if (iVar2 == 0) goto LAB_02091440;
    FUN_02063328(0x300001);
    FUN_0208ada0(0x20c032b,0x20cec01);
  }
  param_4 = 0xffffffffffffffff;
LAB_02091440:
  FUN_0209150c(param_4);
  return;
}


