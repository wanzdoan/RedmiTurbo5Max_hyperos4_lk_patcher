// ffff00009070dee0  size=592  FUN_ffff00009070dee0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009070dee0(char *param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined8 uVar3;
  long lVar4;
  char *pcVar5;
  char *pcVar6;
  char *pcVar7;
  ulong uVar8;
  long lVar9;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined4 local_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined4 local_48;
  
  local_48 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_60 = 0;
  local_70 = 0;
  uStack_68 = 0;
  if (param_1 != (char *)0x0) {
    do {
      pcVar7 = param_1;
      param_1 = pcVar7 + 1;
    } while (*pcVar7 == '\n');
    pcVar5 = pcVar7 + 1;
    if (*pcVar7 != '\0') {
      do {
        pcVar6 = pcVar5;
        pcVar5 = pcVar6 + 1;
        if (*pcVar6 == '\0') {
          pcVar5 = (char *)0x0;
          goto LAB_ffff00009070df2c;
        }
      } while (*pcVar6 != '\n');
      *pcVar6 = '\0';
      goto LAB_ffff00009070df2c;
    }
  }
  pcVar5 = (char *)0x0;
  pcVar7 = (char *)0x0;
LAB_ffff00009070df2c:
  uVar3 = FUN_ffff00009077ef88(s_CRC_LIST_ffff0000907d256b);
  iVar1 = FUN_ffff00009077e820(pcVar7,s_CRC_LIST_ffff0000907d256b,uVar3);
  if (iVar1 == 0) {
    if (pcVar5 != (char *)0x0) {
      do {
        pcVar7 = pcVar5;
        pcVar5 = pcVar7 + 1;
      } while (*pcVar7 == '\n');
      if (*pcVar7 != '\0') {
        do {
          pcVar6 = pcVar5;
          pcVar5 = pcVar6 + 1;
          if (*pcVar6 == '\0') {
            pcVar5 = (char *)0x0;
            goto LAB_ffff00009070e068;
          }
        } while (*pcVar6 != '\n');
        *pcVar6 = '\0';
LAB_ffff00009070e068:
        uVar8 = 0;
        while( true ) {
          iVar1 = FUN_ffff00009070de24(pcVar7,&local_58,&local_70);
          if (iVar1 != 0) {
            DAT_ffff00009083df84 = 0;
            return 1;
          }
          lVar9 = uVar8 * 0x18 + -0xffff6f7c237c;
          lVar4 = FUN_ffff00009077ef88(&local_58);
          FUN_ffff00009077efe4(lVar9,&local_58,lVar4 + 1);
          uVar2 = FUN_ffff00009077d670(&local_70,0,0x10);
          *(undefined4 *)(&DAT_ffff00009083dc98 + uVar8 * 0x18) = uVar2;
          FUN_ffff00009077de54
                    (s_crc_list__d__partition_name__s_v_ffff000090801d04,uVar8 & 0xffffffff,lVar9);
          if (pcVar5 == (char *)0x0) break;
          do {
            pcVar7 = pcVar5;
            pcVar5 = pcVar7 + 1;
          } while (*pcVar7 == '\n');
          if (*pcVar7 == '\0') break;
          do {
            pcVar6 = pcVar5;
            pcVar5 = pcVar6 + 1;
            if (*pcVar6 == '\0') {
              pcVar5 = (char *)0x0;
              goto LAB_ffff00009070e10c;
            }
          } while (*pcVar6 != '\n');
          *pcVar6 = '\0';
LAB_ffff00009070e10c:
          uVar8 = uVar8 + 1;
          if (uVar8 == 0x20) {
            DAT_ffff00009083df84 = 0;
            return 1;
          }
        }
      }
    }
    uVar3 = 0;
    DAT_ffff00009083df84 = 1;
  }
  else {
    DAT_ffff00009083df84 = 0;
    uVar3 = 1;
    _DAT_ffff00009083dc98 = 0;
    _DAT_ffff00009083dcb0 = 0;
    _DAT_ffff00009083dcc8 = 0;
    _DAT_ffff00009083dce0 = 0;
    _DAT_ffff00009083dcf8 = 0;
    _DAT_ffff00009083dd10 = 0;
    _DAT_ffff00009083dd28 = 0;
    _DAT_ffff00009083dd40 = 0;
    _DAT_ffff00009083dd58 = 0;
    _DAT_ffff00009083dd70 = 0;
    _DAT_ffff00009083dd88 = 0;
    _DAT_ffff00009083dda0 = 0;
    _DAT_ffff00009083ddb8 = 0;
    _DAT_ffff00009083ddd0 = 0;
    _DAT_ffff00009083dde8 = 0;
    _DAT_ffff00009083de00 = 0;
    _DAT_ffff00009083de18 = 0;
    _DAT_ffff00009083de30 = 0;
    _DAT_ffff00009083de48 = 0;
    _DAT_ffff00009083de60 = 0;
    _DAT_ffff00009083de78 = 0;
    _DAT_ffff00009083de90 = 0;
    _DAT_ffff00009083dea8 = 0;
    _DAT_ffff00009083dec0 = 0;
    _DAT_ffff00009083ded8 = 0;
    _DAT_ffff00009083def0 = 0;
    _DAT_ffff00009083df08 = 0;
    _DAT_ffff00009083df20 = 0;
    _DAT_ffff00009083df38 = 0;
    _DAT_ffff00009083df50 = 0;
    _DAT_ffff00009083df68 = 0;
    _DAT_ffff00009083df80 = 0;
  }
  return uVar3;
}


