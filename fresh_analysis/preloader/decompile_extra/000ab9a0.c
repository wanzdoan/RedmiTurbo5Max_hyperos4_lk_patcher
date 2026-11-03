// File offset entry: 0xab9a0

undefined8
FUN_020ab8a0(long param_1,uint param_2,long param_3,uint param_4,long param_5,int param_6,
            int *param_7)

{
  uint uVar1;
  undefined8 uVar2;
  
  uVar2 = 0x7247;
  if ((((param_6 != 0) && (param_4 != 0)) && (param_1 != 0)) &&
     (((param_3 != 0 && (param_5 != 0)) && (param_7 != (int *)0x0)))) {
    if (param_2 < param_4 + param_6 + 2) {
      uVar2 = 0x7246;
    }
    else {
      FUN_020915ac(param_1,param_3,param_4);
      *(undefined1 *)(param_1 + (ulong)param_4) = 0;
      FUN_020915ac(param_1 + (ulong)(param_4 + 1),param_5,param_6);
      uVar1 = param_4 + 1 + param_6;
      uVar2 = 0;
      *(undefined1 *)(param_1 + (ulong)uVar1) = 0x80;
      *param_7 = uVar1 + 1;
    }
  }
  return uVar2;
}


