
undefined8 FUN_ffff0000b806c394(undefined8 param_1,undefined4 param_2,long param_3)

{
  undefined *puVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  uVar2 = FUN_ffff0000b8067938();
  FUN_ffff0000b804c33c(s__SBC__sbc_en____d_ffff0000b809ff1a,uVar2);
  iVar3 = FUN_ffff0000b8067938();
  puVar1 = PTR_DAT_ffff0000b8108830;
  if (iVar3 == 0) {
    uVar4 = FUN_ffff0000b8069a90
                      (*(undefined8 *)PTR_DAT_ffff0000b8108830,param_3,
                       *(undefined4 *)(param_3 + 0x200));
    if ((int)uVar4 != 0) {
      FUN_ffff0000b804c33c(s_g_sec_oemkey___ffff0000b8092f1f);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar5 = 0;
        do {
          FUN_ffff0000b804c33c(s_0x_x_ffff0000b809f08e,*(undefined1 *)(*(long *)puVar1 + uVar5));
          uVar5 = uVar5 + 1;
          if ((uVar5 & 0xf) == 0) {
            FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
          }
        } while (uVar5 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff0000b804c33c(s_pubk_>n___ffff0000b808c11b);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar5 = 0;
        do {
          FUN_ffff0000b804c33c(s_0x_x_ffff0000b809f08e,*(undefined1 *)(param_3 + uVar5));
          uVar5 = uVar5 + 1;
          if ((uVar5 & 0xf) == 0) {
            FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
          }
        } while (uVar5 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff0000b804c33c(s_pubk_auth_fail_ffff0000b809ff2d);
      uVar4 = 0x7019;
    }
  }
  else {
    FUN_ffff0000b804c33c(s__SBC__sbc_en___1_ffff0000b8085d91);
    puVar1 = PTR_DAT_ffff0000b8108820;
    uVar4 = FUN_ffff0000b80615f4
                      (param_1,param_2,&DAT_ffff0000b81b90c4,*(undefined4 *)PTR_DAT_ffff0000b8108820
                      );
    if ((int)uVar4 == 0) {
      if (*(int *)puVar1 - 1U < 2) {
        uVar4 = FUN_ffff0000b806795c
                          (&DAT_ffff0000b81b9104,0x30,*(undefined4 *)PTR_DAT_ffff0000b8108868);
        if (((int)uVar4 == 0) &&
           (uVar4 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b90c4,&DAT_ffff0000b81b9104,0x30),
           (int)uVar4 != 0)) {
          FUN_ffff0000b804c33c(s_pubk_auth_fail_ffff0000b809ff2d);
          FUN_ffff0000b804c33c(s_pubk_hash___ffff0000b80a18c8);
          uVar5 = 0;
          do {
            FUN_ffff0000b804c33c(s_0x_x_ffff0000b809f08e,(&DAT_ffff0000b81b90c4)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
            }
          } while (uVar5 != 0x30);
          FUN_ffff0000b804c33c(s_pubk_hash_fuse___ffff0000b808d94c);
          uVar5 = 0;
          do {
            FUN_ffff0000b804c33c(s_0x_x_ffff0000b809f08e,(&DAT_ffff0000b81b9104)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
            }
            uVar4 = 0x7019;
          } while (uVar5 != 0x30);
        }
      }
      else if (*(int *)puVar1 == 0) {
        uVar4 = FUN_ffff0000b806795c
                          (&DAT_ffff0000b81b9104,0x20,*(undefined4 *)PTR_DAT_ffff0000b8108868);
        if ((((int)uVar4 == 0) &&
            (uVar4 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b90c4,&DAT_ffff0000b81b9104,0x20),
            (int)uVar4 != 0)) && (uVar4 = FUN_ffff0000b806c71c(param_1,param_2), (int)uVar4 != 0)) {
          FUN_ffff0000b804c33c(s_pubk_auth_fail_ffff0000b809ff2d);
          FUN_ffff0000b804c33c(s_pubk_hash___ffff0000b80a18c8);
          uVar5 = 0;
          do {
            FUN_ffff0000b804c33c(s_0x_x_ffff0000b809f08e,(&DAT_ffff0000b81b90c4)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
            }
          } while (uVar5 != 0x20);
          FUN_ffff0000b804c33c(s_pubk_hash_fuse___ffff0000b808d94c);
          uVar5 = 0;
          do {
            FUN_ffff0000b804c33c(s_0x_x_ffff0000b809f08e,(&DAT_ffff0000b81b9104)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
            }
            uVar4 = 0x7019;
          } while (uVar5 != 0x20);
        }
      }
      else {
        uVar4 = 0x101023;
      }
    }
  }
  return uVar4;
}


