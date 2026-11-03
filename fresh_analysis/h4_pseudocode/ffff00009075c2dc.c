
undefined8 FUN_ffff00009075c2dc(long *param_1)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((((param_1 == (long *)0x0) || (*param_1 == 0)) || (lVar2 = param_1[1], lVar2 == 0)) ||
     (*(code **)(lVar2 + 0x28) == (code *)0x0)) {
    uVar1 = 0xffffffff;
  }
  else {
    (**(code **)(lVar2 + 0x28))();
    if (*(code **)(lVar2 + 0x58) != (code *)0x0) {
      (**(code **)(lVar2 + 0x58))();
    }
    uVar1 = 0;
  }
  return uVar1;
}


