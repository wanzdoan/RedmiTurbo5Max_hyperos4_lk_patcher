
void FUN_ffff0000be0180cc(int *param_1,undefined4 *param_2,uint param_3)

{
  undefined4 *puVar1;
  undefined4 uVar2;
  long lVar3;
  ulong uVar4;
  int iVar5;
  long lVar6;
  ulong uVar7;
  
  *(undefined4 **)(param_1 + 4) = param_2;
  param_1[2] = param_3;
LAB_ffff0000be018170:
  do {
    if (param_3 == 0) break;
    switch(param_1[1]) {
    case 0:
      if (param_3 < 0x1c) goto code_r0xffff0000be018164;
      FUN_ffff0000be060ae0(&DAT_ffff0000be108750,0,0x50);
      FUN_ffff0000be0607b0(&DAT_ffff0000be108750,param_2,0x1c);
      DAT_ffff0000be108790 = 0;
      DAT_ffff0000be108778 = 0;
      param_3 = param_3 - DAT_ffff0000be108758;
      param_2 = (undefined4 *)((long)param_2 + (ulong)DAT_ffff0000be108758);
      goto LAB_ffff0000be018354;
    case 1:
      if (DAT_ffff0000be108764 <= DAT_ffff0000be108778) {
        param_1[2] = 0;
        param_1[0] = 0;
        param_1[1] = 1;
        return;
      }
      if (DAT_ffff0000be108790 + param_3 < (uint)DAT_ffff0000be10875a) {
        DAT_ffff0000be108790 = param_3;
        FUN_ffff0000be0607b0(&DAT_ffff0000be108794,param_2,param_3);
        param_1[2] = 0;
        *(undefined4 **)(param_1 + 4) = param_2;
        param_1[0] = 0;
        param_1[1] = 1;
        return;
      }
      if (DAT_ffff0000be108790 == 0) {
        FUN_ffff0000be0607b0(&DAT_ffff0000be10876c,param_2,0xc);
        uVar7 = (ulong)DAT_ffff0000be10875a;
      }
      else {
        uVar7 = (ulong)(DAT_ffff0000be10875a - DAT_ffff0000be108790);
        FUN_ffff0000be0607b0(&DAT_ffff0000be10876c,&DAT_ffff0000be108794);
        uVar4 = uVar7;
        if (0xc < (ulong)DAT_ffff0000be10875a) {
          uVar4 = (uVar7 - DAT_ffff0000be10875a) + 0xc;
        }
        FUN_ffff0000be0607b0
                  ((long)&DAT_ffff0000be10876c + (ulong)DAT_ffff0000be108790,param_2,uVar4);
        DAT_ffff0000be108790 = 0;
      }
      DAT_ffff0000be108780 = (ulong)DAT_ffff0000be10875c * (ulong)DAT_ffff0000be108770;
      if (3 < DAT_ffff0000be10876c - 0xcac1) {
        iVar5 = -0x6a;
        goto LAB_ffff0000be01846c;
      }
      param_3 = param_3 - (int)uVar7;
      iVar5 = 2;
      switch((uint)DAT_ffff0000be10876c) {
      case 0xcac2:
        iVar5 = 4;
        break;
      case 0xcac3:
        DAT_ffff0000be108788 = DAT_ffff0000be108788 + DAT_ffff0000be108780;
        iVar5 = 1;
        DAT_ffff0000be108780 = 0;
        break;
      case 0xcac4:
        iVar5 = 5;
      }
      param_2 = (undefined4 *)((long)param_2 + uVar7);
      *param_1 = 0;
      param_1[1] = iVar5;
      param_1[2] = param_3;
      *(undefined4 **)(param_1 + 4) = param_2;
      DAT_ffff0000be108778 = DAT_ffff0000be108778 + 1;
      goto code_r0xffff0000be018164;
    case 2:
      uVar7 = (ulong)param_3;
      if (uVar7 < DAT_ffff0000be108780) {
        if ((param_3 & 0xfff) != 0) {
          if (param_3 < 0x1000) {
            param_1[0] = 100;
            param_1[1] = 2;
            return;
          }
          uVar7 = (ulong)(param_3 & 0xfffff000);
        }
        uVar4 = FUN_ffff0000be05e088
                          (**(undefined8 **)(param_1 + 10),param_2,DAT_ffff0000be108788,uVar7);
        if (uVar4 != uVar7) {
LAB_ffff0000be018468:
          iVar5 = -0x67;
LAB_ffff0000be01846c:
          *param_1 = iVar5;
          return;
        }
        if (*param_1 != 0) {
          return;
        }
        param_2 = (undefined4 *)((long)param_2 + uVar7);
        DAT_ffff0000be108780 = DAT_ffff0000be108780 - uVar7;
        DAT_ffff0000be108788 = DAT_ffff0000be108788 + uVar7;
        iVar5 = 2;
        param_3 = param_3 & 0xfff;
      }
      else {
        uVar7 = FUN_ffff0000be05e088(**(undefined8 **)(param_1 + 10),param_2,DAT_ffff0000be108788);
        if (uVar7 != DAT_ffff0000be108780) goto LAB_ffff0000be018468;
        if (*param_1 != 0) {
          return;
        }
        param_2 = (undefined4 *)((long)param_2 + uVar7);
        param_3 = param_3 - (int)uVar7;
        DAT_ffff0000be108788 = DAT_ffff0000be108788 + uVar7;
        iVar5 = 1;
        DAT_ffff0000be108780 = 0;
      }
      break;
    default:
      goto code_r0xffff0000be018164;
    case 4:
      uVar2 = *param_2;
      lVar3 = FUN_ffff0000be018518();
      lVar6 = 0;
      do {
        puVar1 = (undefined4 *)(lVar3 + lVar6);
        lVar6 = lVar6 + 8;
        *puVar1 = uVar2;
        puVar1[1] = uVar2;
      } while (lVar6 != 0x200000);
      param_2 = param_2 + 1;
      param_3 = param_3 - 4;
      uVar7 = DAT_ffff0000be108780;
      if (0x1fffff < DAT_ffff0000be108780) {
        uVar7 = 0x200000;
      }
      while (uVar7 != 0) {
        uVar4 = FUN_ffff0000be05e088
                          (**(undefined8 **)(param_1 + 10),lVar3,DAT_ffff0000be108788,uVar7);
        if (uVar4 != uVar7) goto LAB_ffff0000be018468;
        if (*param_1 != 0) {
          return;
        }
        DAT_ffff0000be108780 = DAT_ffff0000be108780 - uVar7;
        DAT_ffff0000be108788 = DAT_ffff0000be108788 + uVar7;
        uVar7 = DAT_ffff0000be108780;
        if (0x1fffff < DAT_ffff0000be108780) {
          uVar7 = 0x200000;
        }
      }
LAB_ffff0000be018354:
      iVar5 = 1;
      break;
    case 5:
      param_2 = param_2 + 1;
      iVar5 = 1;
      DAT_ffff0000be108780 = 0;
      param_3 = param_3 - 4;
    }
    *param_1 = 0;
    param_1[1] = iVar5;
    param_1[2] = param_3;
    *(undefined4 **)(param_1 + 4) = param_2;
    if (param_3 == 0) {
      return;
    }
  } while( true );
code_r0xffff0000be018164:
  if (*param_1 != 0) {
    return;
  }
  if (param_3 == 0) {
    return;
  }
  goto LAB_ffff0000be018170;
}


