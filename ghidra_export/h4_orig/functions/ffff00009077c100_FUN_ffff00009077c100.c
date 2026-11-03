// ffff00009077c100  size=68  FUN_ffff00009077c100

undefined8 FUN_ffff00009077c100(long param_1,undefined8 param_2,uint param_3,int param_4)

{
  int iVar1;
  uint uVar2;
  undefined8 uVar3;
  
  uVar2 = *(uint *)(param_1 + 0x38);
  iVar1 = uVar2 - param_3;
  if (param_4 + param_3 <= uVar2) {
    iVar1 = param_4;
  }
  if (((param_4 != 0) && (param_3 <= uVar2)) && (iVar1 != 0)) {
                    /* WARNING: Could not recover jumptable at 0xffff00009077c134. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar3 = (**(code **)(param_1 + 0x78))();
    return uVar3;
  }
  return 0;
}


