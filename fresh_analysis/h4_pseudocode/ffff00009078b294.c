
undefined8
FUN_ffff00009078b294(uint *param_1,uint param_2,uint param_3,undefined4 param_4,int param_5)

{
  if (0 < param_5) {
    do {
      if ((*param_1 & param_2) == param_3) {
        return 0;
      }
      FUN_ffff00009077cea0(param_4);
      param_5 = param_5 + -1;
    } while (param_5 != 0);
  }
  return 0xffffff8c;
}


