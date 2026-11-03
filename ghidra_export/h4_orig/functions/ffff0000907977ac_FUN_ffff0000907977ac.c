// ffff0000907977ac  size=804  FUN_ffff0000907977ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907977ac(int *param_1,long *param_2,int *param_3)

{
  int *piVar1;
  int iVar2;
  bool bVar3;
  undefined8 uVar4;
  int iVar5;
  long *plVar6;
  long lVar7;
  
  iVar5 = *param_1;
  piVar1 = param_1;
  do {
    if (iVar5 != 0x58901690) {
      uVar4 = 0;
      lVar7 = -0x16;
      plVar6 = (long *)&DAT_ffff00009090a918;
      do {
        if (*plVar6 == 0) {
          FUN_ffff00009077e11c
                    (s__s__partition_missing___0x_lx__f_ffff0000907d5373,
                     s__GPUEB__LDR__ffff0000907e0ff2,param_1,(int)lVar7 + 0x17,0);
          uVar4 = 0xfffffff9;
        }
        bVar3 = lVar7 != -1;
        lVar7 = lVar7 + 1;
        plVar6 = plVar6 + 2;
      } while (bVar3);
      return uVar4;
    }
    lVar7 = (long)piVar1 + (ulong)(uint)piVar1[1];
    switch(piVar1[4]) {
    case 1:
      iVar5 = piVar1[2];
      *param_2 = lVar7;
      *param_3 = iVar5;
      iVar5 = piVar1[2];
      _DAT_ffff00009090a918 = lVar7;
      _DAT_ffff00009090a920 = iVar5;
      goto LAB_ffff000090797830;
    case 2:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a928;
      break;
    case 3:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a938;
      break;
    case 4:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a948;
      break;
    case 5:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a958;
      break;
    case 6:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a968;
      break;
    case 7:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a978;
      break;
    case 8:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a988;
      break;
    case 9:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a998;
      break;
    case 10:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a9a8;
      break;
    case 0xb:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a9b8;
      break;
    case 0xc:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a9c8;
      break;
    case 0xd:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a9d8;
      break;
    case 0xe:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a9e8;
      break;
    case 0xf:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090a9f8;
      break;
    case 0x10:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090aa08;
      break;
    case 0x11:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090aa18;
      break;
    case 0x12:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090aa28;
      break;
    case 0x13:
      iVar5 = piVar1[2];
      plVar6 = (long *)&DAT_ffff00009090aa38;
      break;
    case 0x14:
      iVar5 = piVar1[2];
      _DAT_ffff00009090aa54 = piVar1[5];
      _DAT_ffff00009090aa48 = lVar7;
      _DAT_ffff00009090aa50 = iVar5;
      goto LAB_ffff000090797830;
    case 0x15:
      iVar5 = piVar1[2];
      _DAT_ffff00009090aa64 = piVar1[5];
      _DAT_ffff00009090aa58 = lVar7;
      _DAT_ffff00009090aa60 = iVar5;
      goto LAB_ffff000090797830;
    case 0x16:
      iVar5 = piVar1[2];
      _DAT_ffff00009090aa74 = piVar1[5];
      _DAT_ffff00009090aa68 = lVar7;
      _DAT_ffff00009090aa70 = iVar5;
      goto LAB_ffff000090797830;
    default:
      FUN_ffff00009077e11c
                (s__s__warning__Ignore_unknown_GPUE_ffff0000907d2159,s__GPUEB__LDR__ffff0000907e0ff2
                );
      iVar5 = piVar1[2];
      goto LAB_ffff000090797830;
    }
    iVar2 = piVar1[5];
    *plVar6 = lVar7;
    *(int *)(plVar6 + 1) = iVar5;
    *(int *)((long)plVar6 + 0xc) = iVar2;
LAB_ffff000090797830:
    piVar1 = (int *)(lVar7 + (ulong)((iVar5 + piVar1[3]) - 1U & -piVar1[3]));
    iVar5 = *piVar1;
  } while( true );
}


