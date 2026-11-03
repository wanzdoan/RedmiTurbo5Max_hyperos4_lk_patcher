// File offset entry: 0xa6cc

undefined8 FUN_0200a5cc(long *param_1,undefined8 param_2)

{
  long lVar1;
  undefined8 uVar2;
  
  lVar1 = func_0x0206b7f0(0x10000,0x10000,0xffffffffffffffff,0,0,param_2);
  if (lVar1 == 0) {
    uVar2 = 0x10a008;
  }
  else {
    uVar2 = 0;
    *param_1 = lVar1;
  }
  return uVar2;
}


