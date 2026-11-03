// File offset entry: 0x3dda8

undefined8 FUN_ffff0000b803dda8(long *param_1,undefined8 param_2)

{
  undefined4 uVar1;
  int iVar2;
  int *piVar3;
  undefined4 uVar4;
  uint uVar5;
  long lVar6;
  undefined1 local_14 [4];
  
  if (((param_1 != (long *)0x0) && (*param_1 != 0)) && (lVar6 = param_1[1], lVar6 != 0)) {
    if (*(code **)(lVar6 + 0x48) != (code *)0x0) {
      (**(code **)(lVar6 + 0x48))(param_2);
    }
    if (*(code **)(lVar6 + 0x20) != (code *)0x0) {
      piVar3 = (int *)*param_1;
      if (((piVar3 == (int *)0x0) || (param_1[1] == 0)) || ((int)param_1[3] == 0)) {
        (**(code **)(lVar6 + 0x20))(param_2);
        piVar3 = (int *)*param_1;
      }
      if (*piVar3 == 2) {
        local_14[0] = 0;
        if (*piVar3 == 2) {
          uVar4 = 0x2b;
          if ((int)param_1[2] == 2) {
            uVar4 = 0x2c;
          }
          uVar1 = 0x2d;
          if ((int)param_1[2] != 3) {
            uVar1 = uVar4;
          }
        }
        else {
          uVar1 = 0x102;
        }
        iVar2 = FUN_ffff0000b8030cc4(uVar1,0,10,local_14,1);
        FUN_ffff0000b804c33c(s_read_from_lcm_0x0A__0x_x__ret__d_ffff0000b809931e,local_14[0],iVar2);
        piVar3 = (int *)*param_1;
        uVar5 = (uint)(iVar2 != 0);
      }
      else {
        uVar5 = 0;
      }
      if (piVar3[0x178] == 1) {
        uVar5 = 1;
      }
      *(uint *)((long)param_1 + 0x1c) = uVar5;
      return 0;
    }
  }
  return 0xffffffff;
}


