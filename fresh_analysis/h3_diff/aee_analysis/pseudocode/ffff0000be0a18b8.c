
undefined8 FUN_ffff0000be0a18b8(long *param_1,long param_2,undefined8 param_3)

{
  long lVar1;
  undefined8 uVar2;
  char *pcVar3;
  long local_18;
  
  pcVar3 = (char *)*param_1;
  if (param_2 - (long)pcVar3 < 1) {
    uVar2 = 0xffffffa0;
  }
  else if (*pcVar3 == '\x02') {
    local_18 = 0;
    *param_1 = (long)(pcVar3 + 1);
    uVar2 = FUN_ffff0000be0a1768(param_1,param_2,&local_18);
    lVar1 = local_18;
    if ((int)uVar2 == 0) {
      uVar2 = FUN_ffff0000be09c3f4(param_3,*param_1,local_18);
      *param_1 = *param_1 + lVar1;
    }
  }
  else {
    uVar2 = 0xffffff9e;
  }
  return uVar2;
}


