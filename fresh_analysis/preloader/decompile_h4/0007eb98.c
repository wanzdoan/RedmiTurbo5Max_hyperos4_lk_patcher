// File offset entry: 0x7eb98

undefined8 FUN_0207ea98(int *param_1,ulong *param_2,ulong *param_3,ulong *param_4,char param_5)

{
  undefined8 uVar1;
  
  if (*param_1 == 0x58881688) {
    *(undefined1 *)((long)param_1 + 0x27) = 0;
    if (param_5 != '\0') {
      func_0x0208ada0(0x20c3094,0x20c744a,param_1 + 2,param_1[10],param_1[0xb],param_1[1],0x58881688
                     );
    }
    uVar1 = 0;
    *param_2 = (ulong)(uint)param_1[10];
    *param_3 = (ulong)(uint)param_1[1];
    *param_4 = (ulong)(uint)param_1[0xb];
  }
  else {
    if (param_5 != '\0') {
      func_0x0208ada0(0x20c521d,0x20c744a);
    }
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


