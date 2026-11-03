
int FUN_ffff0000b80697c4(uint param_1,long param_2)

{
  byte bVar1;
  int iVar2;
  int iVar3;
  undefined8 in_x6;
  undefined8 in_x7;
  uint *puVar4;
  undefined8 unaff_x19;
  undefined8 unaff_x20;
  uint uVar5;
  undefined8 unaff_x21;
  uint uVar6;
  undefined8 unaff_x22;
  ulong uVar7;
  undefined8 unaff_x29;
  undefined8 unaff_x30;
  
  if (param_1 == 4) {
    bVar1 = FUN_ffff0000b805ae60(param_2);
    if (bVar1 != 4) {
      iVar3 = FUN_ffff0000b8069524();
      if (iVar3 == 0) {
        iVar3 = 0x104000;
        uVar5 = *(uint *)(PTR_DAT_ffff0000b81087f0 + 0x454);
        uVar6 = *(uint *)(PTR_DAT_ffff0000b81087f8 + 0x70);
        if (uVar5 == *(uint *)(PTR_DAT_ffff0000b81087f8 + 0x70)) {
          uVar6 = uVar5;
          if (uVar5 < (byte)(&DAT_ffff0000b81076d0)[(ulong)bVar1 * 2]) {
            iVar3 = 0x104001;
          }
          else if (uVar5 < 8) {
            if (uVar5 < (byte)(&DAT_ffff0000b81076d1)[(ulong)bVar1 * 2]) {
              iVar3 = 0;
              (&DAT_ffff0000b81076d1)[(ulong)bVar1 * 2] = (byte)uVar5;
            }
            else {
              iVar3 = 0;
            }
          }
          else {
            iVar3 = 0x104002;
          }
        }
      }
      else {
        uVar5 = 0;
        uVar6 = 0;
      }
      iVar2 = FUN_ffff0000b8069ff4();
      if (iVar2 != 0) {
        FUN_ffff0000b804c33c
                  (s___s__img_sub_group_ver_check___g_ffff0000b80a17eb,&DAT_ffff0000b80987e6,bVar1,
                   uVar5,uVar6,(&DAT_ffff0000b81076d0)[(ulong)bVar1 * 2],in_x6,in_x7,unaff_x29,
                   unaff_x30,unaff_x22,unaff_x21,unaff_x20,unaff_x19);
      }
      iVar2 = FUN_ffff0000b8069ff4();
      if (iVar3 == 0) {
        if (iVar2 != 0) {
          FUN_ffff0000b804c33c(s____ok_ffff0000b80a0be2);
        }
      }
      else if (iVar2 != 0) {
        FUN_ffff0000b804c33c(s____fail__0x_x__ffff0000b80a1839,iVar3);
      }
      return iVar3;
    }
    if ((param_2 == 0) ||
       ((iVar3 = FUN_ffff0000b804d248(param_2,PTR_s_lk_main_dtb_ffff0000b81076c0), iVar3 != 0 &&
        (iVar3 = FUN_ffff0000b804d248(param_2,PTR_DAT_ffff0000b81076c8), iVar3 != 0)))) {
      iVar3 = FUN_ffff0000b8069ff4();
      if (iVar3 != 0) {
        FUN_ffff0000b804c33c
                  (s___s___s_not_defined_in_sub_table_ffff0000b809b1bb,&DAT_ffff0000b80987e6,param_2
                  );
        return 0x10400f;
      }
      return 0x10400f;
    }
  }
  else {
    iVar3 = FUN_ffff0000b8069050();
    if (iVar3 == 0) {
      iVar3 = 0x104009;
      uVar5 = *(uint *)(PTR_DAT_ffff0000b81087f0 + 0x454);
      uVar6 = *(uint *)(PTR_DAT_ffff0000b81087f8 + 0x70);
      if (DAT_ffff0000b81b722c == '\x01') {
        if (DAT_ffff0000b81b7230 != 0) {
          uVar7 = 0;
          puVar4 = (uint *)(PTR_DAT_ffff0000b81087d0 + 0x18);
          do {
            if (puVar4[-6] == param_1) {
              if (uVar5 == uVar6) {
                uVar6 = uVar5;
                if (uVar5 < puVar4[-2]) {
                  iVar3 = 0x104001;
                }
                else if (puVar4[-1] < uVar5) {
                  iVar3 = 0x104002;
                }
                else if (uVar5 < *puVar4) {
                  iVar3 = 0;
                  *puVar4 = uVar5;
                }
                else {
                  iVar3 = 0;
                }
              }
              else {
                iVar3 = 0x104000;
              }
              goto LAB_ffff0000b806990c;
            }
            uVar7 = uVar7 + 1;
            puVar4 = puVar4 + 7;
          } while (DAT_ffff0000b81b7230 != uVar7);
        }
        uVar7 = 1;
      }
      else {
        uVar7 = 0;
        iVar3 = 0x10400c;
      }
    }
    else {
      uVar5 = 0;
      uVar7 = 0;
      param_1 = 0;
      uVar6 = 0;
    }
LAB_ffff0000b806990c:
    iVar2 = FUN_ffff0000b8069ff4();
    if (iVar2 != 0) {
      FUN_ffff0000b804c33c
                (s___s__img_ver_check___group__key_v_ffff0000b80920d9,&DAT_ffff0000b80987e6,param_1,
                 uVar5,uVar6,
                 *(undefined4 *)(PTR_DAT_ffff0000b81087d0 + (uVar7 & 0xffffffff) * 0x1c + 0x10));
    }
    iVar2 = FUN_ffff0000b8069ff4();
    if (iVar3 != 0) {
      if (iVar2 == 0) {
        return iVar3;
      }
      FUN_ffff0000b804c33c(s____fail__0x_x__ffff0000b80a1839,iVar3);
      return iVar3;
    }
    if (iVar2 != 0) {
      FUN_ffff0000b804c33c(s____ok_ffff0000b80a0be2);
    }
  }
  return 0;
}


