
undefined8 FUN_ffff00009075c334(long *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  code *pcVar2;
  
  if ((((param_1 == (long *)0x0) || (*param_1 == 0)) || (param_1[1] == 0)) ||
     (pcVar2 = *(code **)(param_1[1] + 0x90), pcVar2 == (code *)0x0)) {
    uVar1 = 0xffffffff;
  }
  else {
    (*pcVar2)(param_2);
    uVar1 = 0;
  }
  return uVar1;
}


