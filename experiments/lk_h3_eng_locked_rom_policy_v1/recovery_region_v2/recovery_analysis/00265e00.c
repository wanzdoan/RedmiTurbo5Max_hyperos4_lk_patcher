
/* VerifyAndInstallPackage(Package*, bool*, std::vector<std::string, std::allocator<std::string >
   >*, int, int*, RecoveryUI*, bool) */

undefined4
VerifyAndInstallPackage
          (Package *param_1,bool *param_2,vector *param_3,int param_4,int *param_5,
          RecoveryUI *param_6,bool param_7)

{
  string sVar1;
  string sVar2;
  ulong uVar3;
  void *__src;
  size_t sVar4;
  undefined8 *puVar5;
  long lVar6;
  int __fd;
  char **__argv;
  bool bVar7;
  double dVar8;
  undefined5 uVar9;
  undefined5 uVar10;
  undefined3 uVar11;
  undefined5 uVar12;
  undefined8 uVar13;
  bool bVar14;
  bool bVar15;
  int iVar16;
  uint uVar17;
  __pid_t __pid;
  long lVar18;
  MemMapping *this;
  ulong uVar19;
  string *psVar20;
  ZipArchive *pZVar21;
  __tree_node **pp_Var22;
  undefined8 *puVar23;
  int *piVar24;
  undefined8 uVar25;
  FILE *pFVar26;
  char *pcVar27;
  long lVar28;
  FILE *pFVar29;
  long *plVar30;
  void *pvVar31;
  longlong lVar32;
  undefined8 uVar33;
  undefined4 *puVar34;
  ostream *poVar35;
  char *pcVar36;
  string *extraout_x1;
  string *extraout_x1_00;
  string *extraout_x1_01;
  string *extraout_x1_02;
  string *__n;
  FILE *pFVar37;
  uint uVar38;
  __tree_node *p_Var39;
  string *psVar40;
  byte bVar41;
  char *pcVar42;
  char *pcVar43;
  byte *pbVar44;
  FILE *pFVar45;
  FILE *pFVar46;
  undefined4 uVar47;
  undefined4 uVar48;
  byte *pbVar49;
  long *plVar51;
  undefined *puVar52;
  __tree_node *p_Var53;
  double dVar54;
  __tree_node *p_Var55;
  byte *local_590 [2];
  void *local_580;
  FILE *local_578;
  FILE *local_570;
  void *local_568;
  ulong local_560;
  ulong local_558;
  void *local_550;
  undefined8 local_548;
  FILE *local_540;
  long *local_538;
  ulong local_530;
  FILE *local_528;
  char *local_520;
  undefined8 local_518;
  undefined4 local_509;
  undefined1 uStack_505;
  int local_504;
  ulong local_500;
  ulong local_4f8;
  void *local_4f0;
  __tree_node **local_4e8;
  __tree_node *local_4e0;
  undefined8 local_4d8;
  int *piStack_4d0;
  string local_4c8;
  undefined2 uStack_4c7;
  char cStack_4c5;
  undefined4 uStack_4c4;
  string sStack_4c0;
  char acStack_4bf [15];
  string local_4b0;
  char cStack_4af;
  undefined6 uStack_4ae;
  char cStack_4a8;
  char cStack_4a7;
  undefined4 uStack_4a6;
  undefined2 uStack_4a2;
  char cStack_4a0;
  undefined5 uStack_49f;
  undefined1 uStack_49a;
  undefined1 uStack_499;
  string local_490;
  char cStack_48f;
  undefined2 uStack_48e;
  int iStack_48c;
  char cStack_488;
  undefined5 uStack_487;
  undefined2 uStack_482;
  char cStack_480;
  undefined2 uStack_47f;
  char cStack_47d;
  undefined2 uStack_47c;
  undefined1 uStack_47a;
  undefined1 uStack_479;
  long local_88;
  byte *pbVar50;

  lVar6 = tpidr_el0;
  local_88 = *(long *)(lVar6 + 0x28);
  (**(code **)(*(long *)param_6 + 0x118))(param_6,1);
  (**(code **)(*(long *)param_6 + 0x128))(param_6,2);
  (**(code **)(*(long *)param_6 + 0x130))(0x3d4ccccd,0x42700000,param_6);
  lVar18 = gettime_ns();
  iVar16 = (**(code **)(*(long *)param_1 + 0x38))(param_1);
  if (iVar16 == 0) {
    lVar28 = __dynamic_cast(param_1,&PTR_vtable_00428698,&PTR_vtable_004286b0,0);
    if (lVar28 == 0) {
      uVar19 = android::base::ShouldLog(4,0);
      if ((uVar19 & 1) == 0) goto LAB_00266080;
      puVar34 = (undefined4 *)__errno();
      uVar48 = *puVar34;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x244,4,0,
                 0xffffffff);
      poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar35,"failed to dynamic_cast to MemoryPackage\n",0x28);
LAB_00267a58:
      android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
      uVar47 = 2;
      *puVar34 = uVar48;
    }
    else {
      this = *(MemMapping **)(lVar28 + 0x50);
      if (this != (MemMapping *)0x0) goto LAB_00265fdc;
      uVar19 = android::base::ShouldLog(4,0);
      if ((uVar19 & 1) != 0) {
        puVar34 = (undefined4 *)__errno();
        uVar48 = *puVar34;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x249,4,0,
                   0xffffffff);
        poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar35,"failed to create memmapping",0x1b);
        goto LAB_00267a58;
      }
LAB_00266080:
      uVar47 = 2;
    }
  }
  else {
    iVar16 = (**(code **)(*(long *)param_1 + 0x38))(param_1);
    if (iVar16 == 1) {
      this = operator_new(0x28);
      *(undefined8 *)(this + 0x20) = 0;
      uVar12 = s__sideload_package_zip_001467e1._16_5_;
      uVar11 = s__sideload_package_zip_001467e1._13_3_;
      uVar9 = s__sideload_package_zip_001467e1._8_5_;
      uVar33 = s__sideload_package_zip_001467e1._0_8_;
      uStack_47a = 0;
      *(undefined8 *)(this + 8) = 0;
      *(undefined8 *)this = 0;
      *(undefined8 *)(this + 0x18) = 0;
      *(undefined8 *)(this + 0x10) = 0;
      uVar10 = s__sideload_package_zip_001467e1._8_5_;
      local_490 = (string)0x2a;
      s__sideload_package_zip_001467e1[8] = (char)uVar9;
      s__sideload_package_zip_001467e1[9] = SUB51(uVar9,1);
      s__sideload_package_zip_001467e1[10] = SUB51(uVar9,2);
      s__sideload_package_zip_001467e1[0xb] = SUB51(uVar9,3);
      s__sideload_package_zip_001467e1[0xc] = SUB51(uVar9,4);
      uStack_487._0_1_ = s__sideload_package_zip_001467e1[8];
      uStack_487._1_1_ = s__sideload_package_zip_001467e1[9];
      uStack_487._2_1_ = s__sideload_package_zip_001467e1[10];
      uStack_487._3_1_ = s__sideload_package_zip_001467e1[0xb];
      uStack_487._4_1_ = s__sideload_package_zip_001467e1[0xc];
      cStack_48f = (char)uVar33;
      uStack_48e = SUB82(uVar33,1);
      iStack_48c = SUB84(uVar33,3);
      cStack_488 = SUB81(uVar33,7);
      uStack_482 = (undefined2)uVar11;
      cStack_480 = SUB31(uVar11,2);
      uStack_47f = (undefined2)uVar12;
      cStack_47d = SUB51(uVar12,2);
      uStack_47c = SUB52(uVar12,3);
      s__sideload_package_zip_001467e1._8_5_ = uVar10;
      uVar19 = MemMapping::MapFile(this,&local_490);
      if (((byte)local_490 & 1) != 0) {
        operator_delete((void *)CONCAT17(uStack_479,
                                         CONCAT16(uStack_47a,
                                                  CONCAT24(uStack_47c,
                                                           CONCAT13(cStack_47d,
                                                                    CONCAT21(uStack_47f,cStack_480))
                                                          ))));
      }
      if ((uVar19 & 1) == 0) {
        uVar19 = android::base::ShouldLog(4,0);
        if ((uVar19 & 1) != 0) {
          puVar34 = (undefined4 *)__errno();
          uVar48 = *puVar34;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x24f,4,0,
                     0xffffffff);
          poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
          std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                    (poVar35,"failed to map file",0x12);
          android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
          *puVar34 = uVar48;
        }
        MemMapping::~MemMapping(this);
        operator_delete(this);
        goto LAB_00266080;
      }
    }
    else {
      this = (MemMapping *)0x0;
    }
LAB_00265fdc:
    signal(7,sig_bus);
    if (param_7) {
      iVar16 = check_identity(DAT_0057b3f0,this);
      if (iVar16 == 0) {
        bVar14 = false;
LAB_002660f4:
        iVar16 = (**(code **)(*(long *)param_1 + 0x38))(param_1);
        if ((this != (MemMapping *)0x0) && (iVar16 == 1)) {
          MemMapping::~MemMapping(this);
          operator_delete(this);
        }
        if (!bVar14) {
          iVar16 = (**(code **)(*(long *)param_1 + 0x38))(param_1);
          uVar19 = verify_package(param_1,param_6,iVar16 == 1);
          if ((uVar19 & 1) == 0) {
            android::base::StringPrintf("error: %d",&local_490,0x15);
            puVar23 = *(undefined8 **)(param_3 + 8);
            if (puVar23 < *(undefined8 **)(param_3 + 0x10)) {
              puVar23[2] = CONCAT17(uStack_479,
                                    CONCAT16(uStack_47a,
                                             CONCAT24(uStack_47c,
                                                      CONCAT13(cStack_47d,
                                                               CONCAT21(uStack_47f,cStack_480)))));
              puVar23[1] = CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
              *puVar23 = CONCAT44(iStack_48c,CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490)));
              *(undefined8 **)(param_3 + 8) = puVar23 + 3;
            }
            else {
              psVar20 = std::vector<std::string,std::allocator<std::string>>::
                        __push_back_slow_path<std::string>
                                  ((vector<std::string,std::allocator<std::string>> *)param_3,
                                   &local_490);
              *(string **)(param_3 + 8) = psVar20;
              if (((byte)local_490 & 1) != 0) {
                operator_delete((void *)CONCAT17(uStack_479,
                                                 CONCAT16(uStack_47a,
                                                          CONCAT24(uStack_47c,
                                                                   CONCAT13(cStack_47d,
                                                                            CONCAT21(uStack_47f,
                                                                                     cStack_480)))))
                               );
              }
            }
            signal(7,(__sighandler_t)0x0);
            uVar47 = 9;
            goto LAB_00267898;
          }
        }
        uVar19 = android::base::ShouldLog(4,0);
        if ((uVar19 & 1) != 0) {
          puVar34 = (undefined4 *)__errno();
          uVar48 = *puVar34;
          android::base::LogMessage::LogMessage
                    ((LogMessage *)&local_4b0,"bootable/recovery/install/install.cpp",0x27e,4,0,
                     0xffffffff);
          poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_4b0);
          poVar35 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar35,"Open update package took:",0x19);
          lVar28 = gettime_ns();
          std::to_string((double)(ulong)(lVar28 - lVar18) / 1000000000.0);
          std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
                    (poVar35,&local_490);
          if (((byte)local_490 & 1) != 0) {
            operator_delete((void *)CONCAT17(uStack_479,
                                             CONCAT16(uStack_47a,
                                                      CONCAT24(uStack_47c,
                                                               CONCAT13(cStack_47d,
                                                                        CONCAT21(uStack_47f,
                                                                                 cStack_480))))));
          }
          android::base::LogMessage::~LogMessage((LogMessage *)&local_4b0);
          *puVar34 = uVar48;
        }
        signal(7,(__sighandler_t)0x0);
        (**(code **)(*(long *)param_6 + 0x158))(param_6,"Installing update...\n");
        if (0 < param_4) {
          (**(code **)(*(long *)param_6 + 0x158))(param_6,"Retry attempt: %d\n",param_4);
        }
        (**(code **)(*(long *)param_6 + 0x1c0))(param_6,0);
        local_4d8 = 0;
        local_4e0 = (__tree_node *)0x0;
        local_4e8 = &local_4e0;
        piStack_4d0 = param_5;
        pZVar21 = (ZipArchive *)(**(code **)(*(long *)param_1 + 0x48))(param_1);
        uVar19 = ReadMetadataFromPackage(pZVar21,(map *)&local_4e8);
        if ((uVar19 & 1) == 0) {
          uVar19 = android::base::ShouldLog(4,0);
          if ((uVar19 & 1) == 0) {
            uVar47 = 2;
          }
          else {
            puVar34 = (undefined4 *)__errno();
            uVar48 = *puVar34;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x163,4,0,
                       0xffffffff);
            poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar35,"Failed to parse metadata in the zip file",0x28);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
            uVar47 = 2;
            *puVar34 = uVar48;
          }
        }
        else {
          cStack_4a7 = 0;
          local_4b0 = (string)0x10;
          cStack_4af = 0x6f;
          uStack_4ae = 0x7079742d6174;
          cStack_4a8 = 0x65;
          pp_Var22 = (__tree_node **)
                     std::
                     __tree<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::__map_value_compare<std::string,std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>>>
                     ::find<std::string>((__tree<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::__map_value_compare<std::string,std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>>>
                                          *)&local_4e8,&local_4b0);
          if (&local_4e0 == pp_Var22) {
            uVar38 = 0;
            local_490 = (string)0x0;
            cStack_48f = '\0';
          }
          else {
            if (((ulong)pp_Var22[7] & 1) == 0) {
              p_Var55 = pp_Var22[8];
              p_Var53 = pp_Var22[7];
              p_Var39 = pp_Var22[9];
              cStack_488 = (char)p_Var55;
              uStack_487 = (undefined5)((ulong)p_Var55 >> 8);
              uStack_482 = (undefined2)((ulong)p_Var55 >> 0x30);
              local_490 = SUB81(p_Var53,0);
              cStack_48f = (char)((ulong)p_Var53 >> 8);
              uStack_48e = (undefined2)((ulong)p_Var53 >> 0x10);
              iStack_48c = (int)((ulong)p_Var53 >> 0x20);
              cStack_480 = (char)p_Var39;
              uStack_47f = (undefined2)((ulong)p_Var39 >> 8);
              cStack_47d = (char)((ulong)p_Var39 >> 0x18);
              uStack_47c = (undefined2)((ulong)p_Var39 >> 0x20);
              uStack_47a = (undefined1)((ulong)p_Var39 >> 0x30);
              uStack_479 = (undefined1)((ulong)p_Var39 >> 0x38);
            }
            else {
              std::string::__init_copy_ctor_external
                        (&local_490,(char *)pp_Var22[9],(ulong)pp_Var22[8]);
            }
            uVar38 = (uint)(byte)local_490;
          }
          __n = (string *)CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
          bVar14 = false;
          psVar40 = (string *)(ulong)(uVar38 >> 1);
          local_4c8 = (string)0x4;
          psVar20 = psVar40;
          if ((uVar38 & 1) != 0) {
            psVar20 = __n;
          }
          cStack_4c5 = '\0';
          uStack_4c7 = 0x4241;
          if (psVar20 == (string *)0x2) {
            pcVar36 = (char *)((ulong)&local_4c8 | 1);
            if ((uVar38 & 1) != 0) {
              pvVar31 = (void *)CONCAT17(uStack_479,
                                         CONCAT16(uStack_47a,
                                                  CONCAT24(uStack_47c,
                                                           CONCAT13(cStack_47d,
                                                                    CONCAT21(uStack_47f,cStack_480))
                                                          )));
              iVar16 = memcmp(pvVar31,pcVar36,(size_t)__n);
              bVar14 = iVar16 == 0;
              goto LAB_002663e8;
            }
            if (1 < uVar38) {
              pcVar27 = (char *)((ulong)&local_490 | 1);
              do {
                if (*pcVar27 != *pcVar36) {
                  bVar14 = false;
                  goto LAB_002663e0;
                }
                psVar40 = psVar40 + -1;
                pcVar27 = pcVar27 + 1;
                pcVar36 = pcVar36 + 1;
              } while (psVar40 != (string *)0x0);
              bVar14 = true;
              goto LAB_002663e0;
            }
            bVar14 = true;
          }
          else {
LAB_002663e0:
            if ((uVar38 & 1) != 0) {
              pvVar31 = (void *)CONCAT17(uStack_479,
                                         CONCAT16(uStack_47a,
                                                  CONCAT24(uStack_47c,
                                                           CONCAT13(cStack_47d,
                                                                    CONCAT21(uStack_47f,cStack_480))
                                                          )));
LAB_002663e8:
              operator_delete(pvVar31);
            }
          }
          if (((byte)local_4b0 & 1) != 0) {
            operator_delete((void *)CONCAT17(uStack_499,
                                             CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0))));
          }
          cStack_47d = 0;
          local_490 = (string)0x24;
          uStack_47f = 0x6574;
          uStack_487 = (undefined5)s_ro_build_ab_update_00143370._8_8_;
          uStack_482 = SUB82(s_ro_build_ab_update_00143370._8_8_,5);
          cStack_480 = SUB81(s_ro_build_ab_update_00143370._8_8_,7);
          cStack_48f = (char)s_ro_build_ab_update_00143370._0_8_;
          uStack_48e = SUB82(s_ro_build_ab_update_00143370._0_8_,1);
          iStack_48c = SUB84(s_ro_build_ab_update_00143370._0_8_,3);
          cStack_488 = SUB81(s_ro_build_ab_update_00143370._0_8_,7);
          uVar38 = android::base::GetBoolProperty((base *)&local_490,(string *)0x0,SUB81(__n,0));
          if (((byte)local_490 & 1) != 0) {
            operator_delete((void *)CONCAT17(uStack_479,
                                             CONCAT16(uStack_47a,
                                                      CONCAT24(uStack_47c,
                                                               CONCAT13(cStack_47d,
                                                                        CONCAT21(uStack_47f,
                                                                                 cStack_480))))));
          }
          pcVar36 = operator_new(0x20);
          pcVar36[0x1a] = '\0';
          uVar13 = s_ro_virtual_ab_allow_non_ab_00153274._18_8_;
          uVar25 = s_ro_virtual_ab_allow_non_ab_00153274._0_8_;
          uVar33 = CONCAT26(s_ro_virtual_ab_allow_non_ab_00153274._16_2_,
                            s_ro_virtual_ab_allow_non_ab_00153274._10_6_);
          cStack_488 = 0x1a;
          uStack_487 = 0;
          uStack_482 = 0;
          cStack_480 = (char)pcVar36;
          uStack_47f = (undefined2)((ulong)pcVar36 >> 8);
          cStack_47d = (char)((ulong)pcVar36 >> 0x18);
          uStack_47c = (undefined2)((ulong)pcVar36 >> 0x20);
          uStack_47a = (undefined1)((ulong)pcVar36 >> 0x30);
          uStack_479 = (undefined1)((ulong)pcVar36 >> 0x38);
          local_490 = (string)0x21;
          cStack_48f = 0;
          uStack_48e = 0;
          iStack_48c = 0;
          *(ulong *)(pcVar36 + 8) =
               CONCAT62(s_ro_virtual_ab_allow_non_ab_00153274._10_6_,
                        s_ro_virtual_ab_allow_non_ab_00153274._8_2_);
          *(undefined8 *)pcVar36 = uVar25;
          *(undefined8 *)(pcVar36 + 0x12) = uVar13;
          *(undefined8 *)(pcVar36 + 10) = uVar33;
          uVar17 = android::base::GetBoolProperty((base *)&local_490,(string *)0x0,SUB81(__n,0));
          if (((byte)local_490 & 1) != 0) {
            operator_delete((void *)CONCAT17(uStack_479,
                                             CONCAT16(uStack_47a,
                                                      CONCAT24(uStack_47c,
                                                               CONCAT13(cStack_47d,
                                                                        CONCAT21(uStack_47f,
                                                                                 cStack_480))))));
          }
          local_500 = 0;
          local_4f8 = 0;
          local_4f0 = (void *)0x0;
          pcVar36 = operator_new(0x20);
          pcVar36[0x1f] = '\0';
          uVar13 = s_ro_build_version_security_patch_0014addb._23_8_;
          uVar25 = s_ro_build_version_security_patch_0014addb._0_8_;
          local_4b0 = (string)0x0;
          cStack_4af = 0;
          uVar33 = CONCAT71(s_ro_build_version_security_patch_0014addb._16_7_,
                            s_ro_build_version_security_patch_0014addb[0xf]);
          cStack_488 = '\x1f';
          uStack_487._0_1_ = '\0';
          uStack_487._1_1_ = '\0';
          uStack_487._2_1_ = '\0';
          uStack_487._3_1_ = '\0';
          uStack_487._4_1_ = '\0';
          uStack_482 = 0;
          cStack_480 = (char)pcVar36;
          uStack_47f = (undefined2)((ulong)pcVar36 >> 8);
          cStack_47d = (char)((ulong)pcVar36 >> 0x18);
          uStack_47c = (undefined2)((ulong)pcVar36 >> 0x20);
          uStack_47a = (undefined1)((ulong)pcVar36 >> 0x30);
          uStack_479 = (undefined1)((ulong)pcVar36 >> 0x38);
          local_490 = (string)0x21;
          cStack_48f = '\0';
          uStack_48e = 0;
          iStack_48c = 0;
          *(ulong *)(pcVar36 + 8) =
               CONCAT17(s_ro_build_version_security_patch_0014addb[0xf],
                        s_ro_build_version_security_patch_0014addb._8_7_);
          *(undefined8 *)pcVar36 = uVar25;
          *(undefined8 *)(pcVar36 + 0x17) = uVar13;
          *(undefined8 *)(pcVar36 + 0xf) = uVar33;
          android::base::GetProperty((base *)&local_490,&local_4b0,__n);
          if (((byte)local_4b0 & 1) != 0) {
            operator_delete((void *)CONCAT17(uStack_499,
                                             CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0))));
          }
          if (((byte)local_490 & 1) != 0) {
            operator_delete((void *)CONCAT17(uStack_479,
                                             CONCAT16(uStack_47a,
                                                      CONCAT24(uStack_47c,
                                                               CONCAT13(cStack_47d,
                                                                        CONCAT21(uStack_47f,
                                                                                 cStack_480))))));
          }
          uVar19 = local_500 >> 1 & 0x7f;
          pvVar31 = (void *)((ulong)&local_500 | 1);
          if ((local_500 & 1) != 0) {
            uVar19 = local_4f8;
            pvVar31 = local_4f0;
          }
          uVar19 = ViolatesSPLDowngrade(pZVar21,pvVar31,uVar19);
          if ((uVar19 & 1) == 0) {
            if (bVar14) {
              iVar16 = (**(code **)(*(long *)param_1 + 0x38))(param_1);
              if (iVar16 != 1) {
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x174,6,
                           0,0xffffffff);
                poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
                poVar35 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                    (poVar35,
                                     "Check failed: package->GetType() == PackageType::kFile",0x36);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar35," ",1);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
              }
LAB_002665d4:
              uVar19 = CheckPackageMetadata(&local_4e8,0);
              if ((uVar19 & 1) == 0) {
                android::base::StringPrintf("error: %d",&local_490,0x1c);
                puVar23 = *(undefined8 **)(param_3 + 8);
                if (puVar23 < *(undefined8 **)(param_3 + 0x10)) {
                  puVar23[2] = CONCAT17(uStack_479,
                                        CONCAT16(uStack_47a,
                                                 CONCAT24(uStack_47c,
                                                          CONCAT13(cStack_47d,
                                                                   CONCAT21(uStack_47f,cStack_480)))
                                                ));
                  puVar23[1] = CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
                  *puVar23 = CONCAT44(iStack_48c,CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490))
                                     );
                  *(undefined8 **)(param_3 + 8) = puVar23 + 3;
                }
                else {
                  psVar20 = std::vector<std::string,std::allocator<std::string>>::
                            __push_back_slow_path<std::string>
                                      ((vector<std::string,std::allocator<std::string>> *)param_3,
                                       &local_490);
                  *(string **)(param_3 + 8) = psVar20;
                  if (((byte)local_490 & 1) != 0) {
                    operator_delete((void *)CONCAT17(uStack_479,
                                                     CONCAT16(uStack_47a,
                                                              CONCAT24(uStack_47c,
                                                                       CONCAT13(cStack_47d,
                                                                                CONCAT21(uStack_47f,
                                                                                         cStack_480)
                                                                               )))));
                  }
                }
                goto LAB_00266548;
              }
            }
            else if ((uVar38 & (uVar17 ^ 1) & 1) != 0) goto LAB_002665d4;
            local_490 = (string)0x0;
            cStack_48f = '\0';
            uStack_48e = 0;
            iStack_48c = 0;
            cStack_488 = '\0';
            uStack_487._0_1_ = '\0';
            uStack_487._1_1_ = '\0';
            uStack_487._2_1_ = '\0';
            uStack_487._3_1_ = '\0';
            uStack_487._4_1_ = '\0';
            uStack_482 = 0;
            cStack_480 = '\0';
            uStack_47f = 0;
            cStack_47d = '\0';
            uStack_47c = 0;
            uStack_47a = 0;
            uStack_479 = 0;
            uStack_49a = 0;
            local_4b0 = (string)0x2a;
            cStack_4a7 = (char)s_pre_build_incremental_0015b7aa._8_5_;
            uStack_4a6 = SUB54(s_pre_build_incremental_0015b7aa._8_5_,1);
            cStack_4af = (char)s_pre_build_incremental_0015b7aa._0_8_;
            uStack_4ae = SUB86(s_pre_build_incremental_0015b7aa._0_8_,1);
            cStack_4a8 = SUB81(s_pre_build_incremental_0015b7aa._0_8_,7);
            uStack_4a2 = (undefined2)s_pre_build_incremental_0015b7aa._13_3_;
            cStack_4a0 = SUB31(s_pre_build_incremental_0015b7aa._13_3_,2);
            uStack_49f._0_1_ = s_pre_build_incremental_0015b7aa[0x10];
            uStack_49f._1_1_ = s_pre_build_incremental_0015b7aa[0x11];
            uStack_49f._2_1_ = s_pre_build_incremental_0015b7aa[0x12];
            uStack_49f._3_1_ = s_pre_build_incremental_0015b7aa[0x13];
            uStack_49f._4_1_ = s_pre_build_incremental_0015b7aa[0x14];
            pp_Var22 = (__tree_node **)
                       std::
                       __tree<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::__map_value_compare<std::string,std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>>>
                       ::find<std::string>((__tree<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::__map_value_compare<std::string,std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>>>
                                            *)&local_4e8,&local_4b0);
            if (&local_4e0 == pp_Var22) {
              local_490 = (string)0x0;
              cStack_48f = '\0';
            }
            else if (((ulong)pp_Var22[7] & 1) == 0) {
              p_Var55 = pp_Var22[8];
              p_Var53 = pp_Var22[7];
              p_Var39 = pp_Var22[9];
              cStack_488 = (char)p_Var55;
              uStack_487 = (undefined5)((ulong)p_Var55 >> 8);
              uStack_482 = (undefined2)((ulong)p_Var55 >> 0x30);
              local_490 = SUB81(p_Var53,0);
              cStack_48f = (char)((ulong)p_Var53 >> 8);
              uStack_48e = (undefined2)((ulong)p_Var53 >> 0x10);
              iStack_48c = (int)((ulong)p_Var53 >> 0x20);
              cStack_480 = (char)p_Var39;
              uStack_47f = (undefined2)((ulong)p_Var39 >> 8);
              cStack_47d = (char)((ulong)p_Var39 >> 0x18);
              uStack_47c = (undefined2)((ulong)p_Var39 >> 0x20);
              uStack_47a = (undefined1)((ulong)p_Var39 >> 0x30);
              uStack_479 = (undefined1)((ulong)p_Var39 >> 0x38);
            }
            else {
              std::string::__init_copy_ctor_external
                        (&local_490,(char *)pp_Var22[9],(ulong)pp_Var22[8]);
            }
            if (((byte)local_4b0 & 1) != 0) {
              operator_delete((void *)CONCAT17(uStack_499,
                                               CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0)))
                             );
            }
            sVar2 = local_490;
            sVar4 = (ulong)((byte)local_490 >> 1);
            if (((byte)local_490 & 1) != 0) {
              sVar4 = CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
            }
            if (sVar4 != 0) {
              uVar19 = sVar4 + 0xe;
              local_4b0 = (string)0x0;
              cStack_4af = 0;
              uStack_4ae = 0;
              cStack_4a8 = 0;
              cStack_4a7 = 0;
              uStack_4a6 = 0;
              uStack_4a2 = 0;
              cStack_4a0 = 0;
              uStack_49f = 0;
              uStack_49a = 0;
              uStack_499 = 0;
              if (uVar19 < 0xfffffffffffffff0) {
                sVar1 = (string)(0x16 < uVar19);
                if ((bool)sVar1) {
                  uVar3 = (uVar19 | 0xf) + 1;
                  puVar23 = operator_new(uVar3);
                  cStack_4af = (char)(uVar3 >> 8);
                  uStack_4ae = (undefined6)(uVar3 >> 0x10);
                  cStack_4a8 = (char)uVar19;
                  cStack_4a7 = (char)(uVar19 >> 8);
                  uStack_4a6 = (undefined4)(uVar19 >> 0x10);
                  uStack_4a2 = (undefined2)(uVar19 >> 0x30);
                  cStack_4a0 = (char)puVar23;
                  uStack_49f = (undefined5)((ulong)puVar23 >> 8);
                  uStack_49a = (undefined1)((ulong)puVar23 >> 0x30);
                  uStack_499 = (undefined1)((ulong)puVar23 >> 0x38);
                }
                else {
                  puVar23 = (undefined8 *)0x0;
                }
                pvVar31 = (void *)((long)&uStack_4a2 + 1);
                puVar5 = (undefined8 *)((ulong)&local_4b0 | 1);
                if ((bool)sVar1) {
                  pvVar31 = (void *)((long)puVar23 + 0xe);
                  puVar5 = puVar23;
                }
                uVar33 = CONCAT62(s_source_build__0014c8d5._8_6_,s_source_build__0014c8d5._6_2_);
                __src = (void *)((ulong)&local_490 | 1);
                if (((byte)sVar2 & 1) != 0) {
                  __src = (void *)CONCAT17(uStack_479,
                                           CONCAT16(uStack_47a,
                                                    CONCAT24(uStack_47c,
                                                             CONCAT13(cStack_47d,
                                                                      CONCAT21(uStack_47f,cStack_480
                                                                              )))));
                }
                *puVar5 = CONCAT26(s_source_build__0014c8d5._6_2_,s_source_build__0014c8d5._0_6_);
                *(undefined8 *)((long)puVar5 + 6) = uVar33;
                local_4b0 = sVar1;
                memmove(pvVar31,__src,sVar4);
                *(undefined1 *)((long)pvVar31 + sVar4) = 0;
                puVar23 = *(undefined8 **)(param_3 + 8);
                if (puVar23 < *(undefined8 **)(param_3 + 0x10)) {
                  puVar23[2] = CONCAT17(uStack_499,
                                        CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0)));
                  puVar23[1] = CONCAT26(uStack_4a2,
                                        CONCAT42(uStack_4a6,CONCAT11(cStack_4a7,cStack_4a8)));
                  *puVar23 = CONCAT62(uStack_4ae,CONCAT11(cStack_4af,local_4b0));
                  *(undefined8 **)(param_3 + 8) = puVar23 + 3;
                }
                else {
                  psVar20 = std::vector<std::string,std::allocator<std::string>>::
                            __push_back_slow_path<std::string>
                                      ((vector<std::string,std::allocator<std::string>> *)param_3,
                                       &local_4b0);
                  *(string **)(param_3 + 8) = psVar20;
                  if (((byte)local_4b0 & 1) != 0) {
                    operator_delete((void *)CONCAT17(uStack_499,
                                                     CONCAT16(uStack_49a,
                                                              CONCAT51(uStack_49f,cStack_4a0))));
                  }
                }
                goto LAB_002667bc;
              }
LAB_00267e48:
              if (*(long *)(lVar6 + 0x28) != local_88) goto LAB_002683d8;
              std::string::__throw_length_error_abi_ne180000_();
LAB_00267e60:
              if (*(long *)(lVar6 + 0x28) != local_88) goto LAB_002683d8;
              std::string::__throw_length_error_abi_ne180000_();
LAB_00267e78:
              __umask_chk(0x12);
              android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                        ((unique_fd_impl<android::base::DefaultCloser> *)&local_504,-1,(void *)0x0);
              local_490 = (string)0x0;
              cStack_48f = '\0';
              uStack_48e = 0;
              iStack_48c = 0;
              cStack_488 = '\0';
              uStack_487._0_1_ = '\0';
              uStack_487._1_1_ = '\0';
              uStack_487._2_1_ = '\0';
              uStack_487._3_1_ = '\0';
              uStack_487._4_1_ = '\0';
              uStack_482 = 0;
              cStack_480 = '\0';
              uStack_47f = 0;
              cStack_47d = '\0';
              uStack_47c = 0;
              uStack_47a = 0;
              uStack_479 = 0;
              StringVectorToNullTerminatedArray((vector *)&local_4c8);
              __argv = (char **)CONCAT44(iStack_48c,
                                         CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490)));
              iVar16 = execv(*__argv,__argv);
              pFVar26 = *(FILE **)PTR_stdout_00429b30;
              uVar33 = *(undefined8 *)
                        CONCAT44(iStack_48c,CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490)));
              piVar24 = (int *)__errno(iVar16);
              pcVar36 = strerror(*piVar24);
              fprintf(pFVar26,"E:Can\'t run %s (%s)\n",uVar33,pcVar36);
              if (*(long *)(lVar6 + 0x28) == local_88) {
                    /* WARNING: Subroutine does not return */
                _exit(1);
              }
              goto LAB_002683d8;
            }
LAB_002667bc:
            local_4b0 = (string)0x0;
            cStack_4af = 0;
            uStack_4ae = 0;
            cStack_4a8 = 0;
            cStack_4a7 = 0;
            uStack_4a6 = 0;
            uStack_4a2 = 0;
            cStack_4a0 = 0;
            uStack_49f = 0;
            uStack_49a = 0;
            uStack_499 = 0;
            acStack_4bf[0xe] = 0;
            local_4c8 = (string)0x2c;
            acStack_4bf[0] = s_post_build_incremental_0014771b[8];
            acStack_4bf[1] = s_post_build_incremental_0014771b[9];
            acStack_4bf[2] = s_post_build_incremental_0014771b[10];
            acStack_4bf[3] = s_post_build_incremental_0014771b[0xb];
            acStack_4bf[4] = s_post_build_incremental_0014771b[0xc];
            acStack_4bf[5] = s_post_build_incremental_0014771b[0xd];
            uStack_4c7 = (undefined2)s_post_build_incremental_0014771b._0_8_;
            cStack_4c5 = SUB81(s_post_build_incremental_0014771b._0_8_,2);
            uStack_4c4 = SUB84(s_post_build_incremental_0014771b._0_8_,3);
            sStack_4c0 = SUB81(s_post_build_incremental_0014771b._0_8_,7);
            acStack_4bf[6] = (char)s_post_build_incremental_0014771b._14_2_;
            acStack_4bf[7] = SUB21(s_post_build_incremental_0014771b._14_2_,1);
            acStack_4bf[8] = s_post_build_incremental_0014771b[0x10];
            acStack_4bf[9] = s_post_build_incremental_0014771b[0x11];
            acStack_4bf[10] = s_post_build_incremental_0014771b[0x12];
            acStack_4bf[0xb] = s_post_build_incremental_0014771b[0x13];
            acStack_4bf[0xc] = s_post_build_incremental_0014771b[0x14];
            acStack_4bf[0xd] = s_post_build_incremental_0014771b[0x15];
            pp_Var22 = (__tree_node **)
                       std::
                       __tree<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::__map_value_compare<std::string,std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>>>
                       ::find<std::string>((__tree<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::__map_value_compare<std::string,std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,chromeos_update_engine::PartitionUpdate_const*>>>
                                            *)&local_4e8,&local_4c8);
            if (&local_4e0 == pp_Var22) {
              local_4b0 = (string)0x0;
              cStack_4af = 0;
            }
            else if (((ulong)pp_Var22[7] & 1) == 0) {
              p_Var55 = pp_Var22[8];
              p_Var53 = pp_Var22[7];
              p_Var39 = pp_Var22[9];
              cStack_4a8 = (char)p_Var55;
              cStack_4a7 = (char)((ulong)p_Var55 >> 8);
              uStack_4a6 = (undefined4)((ulong)p_Var55 >> 0x10);
              uStack_4a2 = (undefined2)((ulong)p_Var55 >> 0x30);
              local_4b0 = SUB81(p_Var53,0);
              cStack_4af = (char)((ulong)p_Var53 >> 8);
              uStack_4ae = (undefined6)((ulong)p_Var53 >> 0x10);
              cStack_4a0 = (char)p_Var39;
              uStack_49f = (undefined5)((ulong)p_Var39 >> 8);
              uStack_49a = (undefined1)((ulong)p_Var39 >> 0x30);
              uStack_499 = (undefined1)((ulong)p_Var39 >> 0x38);
            }
            else {
              std::string::__init_copy_ctor_external
                        (&local_4b0,(char *)pp_Var22[9],(ulong)pp_Var22[8]);
            }
            if (((byte)local_4c8 & 1) != 0) {
              operator_delete((void *)CONCAT17(acStack_4bf[0xe],
                                               CONCAT61(acStack_4bf._8_6_,acStack_4bf[7])));
            }
            bVar41 = (byte)local_4b0 & 1;
            sVar4 = (ulong)((byte)local_4b0 >> 1);
            if (((byte)local_4b0 & 1) != 0) {
              sVar4 = CONCAT26(uStack_4a2,CONCAT42(uStack_4a6,CONCAT11(cStack_4a7,cStack_4a8)));
            }
            if (sVar4 != 0) {
              uVar19 = sVar4 + 0xe;
              local_4c8 = (string)0x0;
              uStack_4c7 = 0;
              cStack_4c5 = '\0';
              uStack_4c4 = 0;
              sStack_4c0 = (string)0x0;
              acStack_4bf[0] = '\0';
              acStack_4bf[1] = '\0';
              acStack_4bf[2] = '\0';
              acStack_4bf[3] = '\0';
              acStack_4bf[4] = '\0';
              acStack_4bf[5] = '\0';
              acStack_4bf[6] = '\0';
              acStack_4bf[7] = '\0';
              acStack_4bf[8] = '\0';
              acStack_4bf[9] = '\0';
              acStack_4bf[10] = '\0';
              acStack_4bf[0xb] = '\0';
              acStack_4bf[0xc] = '\0';
              acStack_4bf[0xd] = '\0';
              acStack_4bf[0xe] = 0;
              if (0xffffffffffffffef < uVar19) goto LAB_00267e60;
              sVar2 = (string)(0x16 < uVar19);
              if ((bool)sVar2) {
                uVar3 = (uVar19 | 0xf) + 1;
                puVar23 = operator_new(uVar3);
                sStack_4c0 = SUB81(uVar19,0);
                acStack_4bf._0_6_ = (undefined6)(uVar19 >> 8);
                acStack_4bf[6] = (char)(uVar19 >> 0x38);
                acStack_4bf[7] = (char)puVar23;
                acStack_4bf._8_6_ = (undefined6)((ulong)puVar23 >> 8);
                acStack_4bf[0xe] = (char)((ulong)puVar23 >> 0x38);
                uStack_4c7 = (undefined2)(uVar3 >> 8);
                cStack_4c5 = (char)(uVar3 >> 0x18);
                uStack_4c4 = (undefined4)(uVar3 >> 0x20);
              }
              else {
                puVar23 = (undefined8 *)0x0;
              }
              pcVar36 = acStack_4bf + 6;
              if ((bool)sVar2) {
                pcVar36 = (char *)((long)puVar23 + 0xe);
              }
              puVar5 = (undefined8 *)((ulong)&local_4c8 | 1);
              if ((bool)sVar2) {
                puVar5 = puVar23;
              }
              uVar33 = CONCAT62(s_target_build__001425f8._8_6_,s_target_build__001425f8._6_2_);
              pvVar31 = (void *)((ulong)&local_4b0 | 1);
              if (bVar41 != 0) {
                pvVar31 = (void *)CONCAT17(uStack_499,
                                           CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0)));
              }
              *puVar5 = CONCAT26(s_target_build__001425f8._6_2_,s_target_build__001425f8._0_6_);
              *(undefined8 *)((long)puVar5 + 6) = uVar33;
              local_4c8 = sVar2;
              memmove(pcVar36,pvVar31,sVar4);
              pcVar36[sVar4] = '\0';
              puVar23 = *(undefined8 **)(param_3 + 8);
              if (puVar23 < *(undefined8 **)(param_3 + 0x10)) {
                puVar23[2] = CONCAT17(acStack_4bf[0xe],CONCAT61(acStack_4bf._8_6_,acStack_4bf[7]));
                puVar23[1] = CONCAT17(acStack_4bf[6],CONCAT61(acStack_4bf._0_6_,sStack_4c0));
                *puVar23 = CONCAT44(uStack_4c4,CONCAT13(cStack_4c5,CONCAT21(uStack_4c7,local_4c8)));
                *(undefined8 **)(param_3 + 8) = puVar23 + 3;
              }
              else {
                psVar20 = std::vector<std::string,std::allocator<std::string>>::
                          __push_back_slow_path<std::string>
                                    ((vector<std::string,std::allocator<std::string>> *)param_3,
                                     &local_4c8);
                *(string **)(param_3 + 8) = psVar20;
                if (((byte)local_4c8 & 1) != 0) {
                  operator_delete((void *)CONCAT17(acStack_4bf[0xe],
                                                   CONCAT61(acStack_4bf._8_6_,acStack_4bf[7])));
                }
              }
            }
            if (((byte)local_4b0 & 1) != 0) {
              operator_delete((void *)CONCAT17(uStack_499,
                                               CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0)))
                             );
            }
            if (((byte)local_490 & 1) != 0) {
              operator_delete((void *)CONCAT17(uStack_479,
                                               CONCAT16(uStack_47a,
                                                        CONCAT24(uStack_47c,
                                                                 CONCAT13(cStack_47d,
                                                                          CONCAT21(uStack_47f,
                                                                                   cStack_480))))));
            }
            local_490 = (string)0x0;
            cStack_48f = '\0';
            uStack_48e = 0;
            iStack_48c = 0;
            local_509 = CONCAT31(0xffffff,(undefined1)local_509);
            uStack_505 = 0xff;
            local_504 = -1;
            iVar16 = pipe2((int *)&local_490,0);
            puVar52 = PTR_android_fdsan_close_with_tag_00429ae8;
            if (iVar16 == 0) {
              android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                        ((unique_fd_impl<android::base::DefaultCloser> *)&local_504,
                         CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490)),(void *)0x0);
              android::base::unique_fd_impl<android::base::DefaultCloser>::reset
                        ((unique_fd_impl<android::base::DefaultCloser> *)((long)&local_509 + 1),
                         iStack_48c,(void *)0x0);
              local_4b0 = (string)0x0;
              cStack_4af = 0;
              uStack_4ae = 0;
              cStack_4a8 = 0;
              cStack_4a7 = 0;
              uStack_4a6 = 0;
              uStack_4a2 = 0;
              cStack_4a0 = 0;
              uStack_49f = 0;
              uStack_49a = 0;
              uStack_499 = 0;
              (**(code **)(*(long *)param_1 + 0x40))(&local_4b0,param_1);
              local_4c8 = (string)0x0;
              uStack_4c7 = 0;
              cStack_4c5 = '\0';
              uStack_4c4 = 0;
              sStack_4c0 = (string)0x0;
              acStack_4bf[0] = '\0';
              acStack_4bf[1] = '\0';
              acStack_4bf[2] = '\0';
              acStack_4bf[3] = '\0';
              acStack_4bf[4] = '\0';
              acStack_4bf[5] = '\0';
              acStack_4bf[6] = '\0';
              acStack_4bf[7] = '\0';
              acStack_4bf[8] = '\0';
              acStack_4bf[9] = '\0';
              acStack_4bf[10] = '\0';
              acStack_4bf[0xb] = '\0';
              acStack_4bf[0xc] = '\0';
              acStack_4bf[0xd] = '\0';
              acStack_4bf[0xe] = 0;
              if (bVar14) {
                uVar19 = SetUpAbUpdateCommands
                                   (&local_4b0,pZVar21,CONCAT13(uStack_505,local_509._1_3_),
                                    (vector *)&local_4c8);
              }
              else {
                uVar19 = SetUpNonAbUpdateCommands
                                   (&local_4b0,pZVar21,param_4,CONCAT13(uStack_505,local_509._1_3_),
                                    (vector *)&local_4c8);
              }
              if ((uVar19 & 1) == 0) {
                android::base::StringPrintf("error: %d",&local_490,0x1c);
                puVar23 = *(undefined8 **)(param_3 + 8);
                if (puVar23 < *(undefined8 **)(param_3 + 0x10)) {
                  puVar23[2] = CONCAT17(uStack_479,
                                        CONCAT16(uStack_47a,
                                                 CONCAT24(uStack_47c,
                                                          CONCAT13(cStack_47d,
                                                                   CONCAT21(uStack_47f,cStack_480)))
                                                ));
                  puVar23[1] = CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
                  *puVar23 = CONCAT44(iStack_48c,CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490))
                                     );
                  *(undefined8 **)(param_3 + 8) = puVar23 + 3;
                }
                else {
                  psVar20 = std::vector<std::string,std::allocator<std::string>>::
                            __push_back_slow_path<std::string>
                                      ((vector<std::string,std::allocator<std::string>> *)param_3,
                                       &local_490);
                  *(string **)(param_3 + 8) = psVar20;
                  if (((byte)local_490 & 1) != 0) {
                    operator_delete((void *)CONCAT17(uStack_479,
                                                     CONCAT16(uStack_47a,
                                                              CONCAT24(uStack_47c,
                                                                       CONCAT13(cStack_47d,
                                                                                CONCAT21(uStack_47f,
                                                                                         cStack_480)
                                                                               )))));
                  }
                }
                uVar47 = 2;
              }
              else {
                __pid = fork();
                if (__pid == -1) {
                  uVar19 = android::base::ShouldLog(4,0);
                  if ((uVar19 & 1) != 0) {
                    puVar34 = (undefined4 *)__errno();
                    uVar48 = *puVar34;
                    android::base::LogMessage::LogMessage
                              ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",
                               0x1bb,4,0,uVar48);
                    poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490)
                    ;
                    std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                              (poVar35,"Failed to fork update binary",0x1c);
                    android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
                    *puVar34 = uVar48;
                  }
                  android::base::StringPrintf("error: %d",&local_490,0x1b);
                  puVar23 = *(undefined8 **)(param_3 + 8);
                  if (puVar23 < *(undefined8 **)(param_3 + 0x10)) {
                    puVar23[2] = CONCAT17(uStack_479,
                                          CONCAT16(uStack_47a,
                                                   CONCAT24(uStack_47c,
                                                            CONCAT13(cStack_47d,
                                                                     CONCAT21(uStack_47f,cStack_480)
                                                                    ))));
                    puVar23[1] = CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
                    *puVar23 = CONCAT44(iStack_48c,
                                        CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490)));
                    *(undefined8 **)(param_3 + 8) = puVar23 + 3;
                    uVar47 = 1;
                  }
                  else {
                    psVar20 = std::vector<std::string,std::allocator<std::string>>::
                              __push_back_slow_path<std::string>
                                        ((vector<std::string,std::allocator<std::string>> *)param_3,
                                         &local_490);
                    *(string **)(param_3 + 8) = psVar20;
                    if (((byte)local_490 & 1) != 0) {
                      operator_delete((void *)CONCAT17(uStack_479,
                                                       CONCAT16(uStack_47a,
                                                                CONCAT24(uStack_47c,
                                                                         CONCAT13(cStack_47d,
                                                                                  CONCAT21(
                                                  uStack_47f,cStack_480))))));
                    }
                    uVar47 = 1;
                  }
                }
                else {
                  if (__pid == 0) goto LAB_00267e78;
                  piVar24 = (int *)__errno();
                  __fd = CONCAT13(uStack_505,local_509._1_3_);
                  iVar16 = *piVar24;
                  if (__fd != -1) {
                    if (puVar52 == (undefined *)0x0) {
                      close(__fd);
                    }
                    else {
                      uVar33 = android_fdsan_create_owner_tag(3,(long)&local_509 + 1);
                      android_fdsan_close_with_tag(__fd,uVar33);
                    }
                  }
                  local_509 = 0xffffff00;
                  uStack_505 = 0xff;
                  *piVar24 = iVar16;
                  local_518 = 0;
                  local_490 = SUB81(&local_509,0);
                  cStack_48f = (char)((ulong)&local_509 >> 8);
                  uStack_48e = (undefined2)((ulong)&local_509 >> 0x10);
                  iStack_48c = (int)((ulong)&local_509 >> 0x20);
                  std::thread::
                  thread<void(&)(int*,std::atomic<bool>const&),int*&,std::reference_wrapper<std::atomic<bool>>,void>
                            ((thread *)&local_518,log_max_temperature,&piStack_4d0,
                             (reference_wrapper *)&local_490);
                  *param_2 = false;
                  lVar18 = gettime_ns();
                  memset(&local_490,0,0x400);
                  iVar16 = local_504;
                  if (PTR_android_fdsan_exchange_owner_tag_00429af0 != (undefined *)0x0) {
                    uVar33 = android_fdsan_create_owner_tag(3,&local_504);
                    uVar25 = android_fdsan_create_owner_tag(3,0);
                    android_fdsan_exchange_owner_tag(iVar16,uVar33,uVar25);
                  }
                  iVar16 = local_504;
                  local_504 = -1;
                  pFVar26 = fdopen(iVar16,"r");
                  if (pFVar26 == (FILE *)0x0) {
                    close(iVar16);
                  }
                  dVar8 = DAT_001420a0;
                  pcVar36 = (char *)((ulong)&local_530 | 1);
                  plVar51 = (long *)((ulong)&local_548 | 1);
                  bVar14 = false;
                  bVar7 = false;
                  while (pFVar37 = pFVar26, pcVar27 = fgets((char *)&local_490,0x400,pFVar26),
                        pcVar27 != (char *)0x0) {
                    if (bVar14) {
                      bVar14 = true;
                    }
                    else {
                      lVar28 = gettime_ns();
                      if ((double)(ulong)(lVar28 - lVar18) / 1000000000.0 <= 1200.0) {
                        bVar14 = false;
                      }
                      else {
                        bVar14 = true;
                        (**(code **)(*(long *)param_6 + 0x1c0))(param_6,1);
                      }
                    }
                    local_530 = 0;
                    local_528 = (FILE *)0x0;
                    local_520 = (char *)0x0;
                    pFVar29 = (FILE *)strlen((char *)&local_490);
                    if ((FILE *)0xffffffffffffffef < pFVar29) {
                      if (*(long *)(lVar6 + 0x28) != local_88) goto LAB_002683d8;
                      std::string::__throw_length_error_abi_ne180000_();
LAB_00267e00:
                      if (*(long *)(lVar6 + 0x28) != local_88) goto LAB_002683d8;
                      std::string::__throw_length_error_abi_ne180000_();
LAB_00267e18:
                      if (*(long *)(lVar6 + 0x28) != local_88) goto LAB_002683d8;
                      std::string::__throw_length_error_abi_ne180000_();
LAB_00267e30:
                      if (*(long *)(lVar6 + 0x28) != local_88) goto LAB_002683d8;
                      std::string::__throw_out_of_range_abi_ne180000_();
                      goto LAB_00267e48;
                    }
                    if (pFVar29 < (FILE *)0x17) {
                      local_530 = CONCAT71(local_530._1_7_,(char)((int)pFVar29 << 1));
                      psVar20 = extraout_x1;
                      pcVar27 = pcVar36;
                      if (pFVar29 != (FILE *)0x0) goto LAB_00266d4c;
                    }
                    else {
                      uVar19 = ((ulong)pFVar29 | 0xf) + 1;
                      pcVar27 = operator_new(uVar19);
                      local_530 = uVar19 | 1;
                      local_528 = pFVar29;
                      local_520 = pcVar27;
LAB_00266d4c:
                      pFVar37 = pFVar29;
                      memcpy(pcVar27,&local_490,(size_t)pFVar29);
                      psVar20 = extraout_x1_00;
                    }
                    pcVar27[(long)pFVar29] = '\0';
                    pcVar27 = pcVar36;
                    pFVar29 = (FILE *)(local_530 >> 1 & 0x7f);
                    if ((local_530 & 1) != 0) {
                      pcVar27 = local_520;
                      pFVar29 = local_528;
                    }
                    if (pFVar29 == (FILE *)0x0) {
                      pFVar45 = (FILE *)0xffffffffffffffff;
                    }
                    else {
                      pcVar42 = pcVar27;
                      pFVar45 = pFVar29;
                      do {
                        pcVar43 = pcVar42;
                        if ((*pcVar42 == '\n') || (*pcVar42 == ' ')) break;
                        pcVar42 = pcVar42 + 1;
                        pFVar45 = (FILE *)(pFVar45[-1]._unused2 + 0x13);
                        pcVar43 = pcVar27 + (long)pFVar29;
                      } while (pFVar45 != (FILE *)0x0);
                      pFVar45 = (FILE *)(pcVar43 + -(long)pcVar27);
                      if (pcVar43 == pcVar27 + (long)pFVar29) {
                        pFVar45 = (FILE *)0xffffffffffffffff;
                      }
                    }
                    local_548 = 0;
                    local_540 = (FILE *)0x0;
                    if (pFVar45 <= pFVar29) {
                      pFVar29 = pFVar45;
                    }
                    local_538 = (long *)0x0;
                    if ((FILE *)0xffffffffffffffef < pFVar29) goto LAB_00267e00;
                    if (pFVar29 < (FILE *)0x17) {
                      local_548 = (ulong)(byte)((int)pFVar29 << 1);
                      plVar30 = plVar51;
                      if (pFVar29 != (FILE *)0x0) goto LAB_00266e20;
                    }
                    else {
                      uVar19 = ((ulong)pFVar29 | 0xf) + 1;
                      plVar30 = operator_new(uVar19);
                      local_548 = uVar19 | 1;
                      local_540 = pFVar29;
                      local_538 = plVar30;
LAB_00266e20:
                      pFVar37 = pFVar29;
                      memmove(plVar30,pcVar27,(size_t)pFVar29);
                      psVar20 = extraout_x1_01;
                    }
                    *(char *)((long)plVar30 + (long)pFVar29) = '\0';
                    bVar41 = (byte)local_548._0_1_ & 1;
                    pFVar46 = (FILE *)(ulong)((byte)local_548._0_1_ >> 1);
                    pFVar29 = pFVar46;
                    if ((local_548 & 1) != 0) {
                      pFVar29 = local_540;
                    }
                    if (pFVar29 != (FILE *)0x0) {
                      local_560 = 0;
                      local_558 = 0;
                      local_550 = (void *)0x0;
                      if (pFVar45 == (FILE *)0xffffffffffffffff) {
LAB_00266f28:
                        if (bVar41 != 0) {
                          pFVar46 = local_540;
                        }
                        if ((long)pFVar46 < 0xc) {
                          if (pFVar46 != (FILE *)0x8) {
                            if (pFVar46 == (FILE *)0xa) {
                              plVar30 = plVar51;
                              if (bVar41 != 0) {
                                plVar30 = local_538;
                              }
                              if (*plVar30 == 0x6361635f65706977 && (short)plVar30[1] == 0x6568) {
                                *param_2 = true;
                                goto LAB_002673fc;
                              }
                            }
                            goto LAB_002670b0;
                          }
                          plVar30 = plVar51;
                          if (bVar41 != 0) {
                            plVar30 = local_538;
                          }
                          if (*plVar30 == 0x73736572676f7270) {
                            local_578 = (FILE *)0x0;
                            local_570 = (FILE *)0x0;
                            local_568 = (void *)0x0;
                            local_590[0] = (byte *)((ulong)local_590[0]._3_5_ << 0x18);
                            local_590[0] = (byte *)CONCAT62(local_590[0]._2_6_,0x2002);
                            android::base::Split
                                      ((base *)&local_560,(string *)local_590,(string *)pFVar37);
                            if (((ulong)local_590[0] & 1) != 0) {
                              operator_delete(local_580);
                            }
                            if ((long)local_570 - (long)local_578 == 0x30) {
                              iVar16 = local_578->_flags;
                              pbVar44 = (byte *)local_578->_IO_read_end;
                              *piVar24 = 0;
                              local_590[0] = (byte *)0x0;
                              if ((iVar16 & 1) == 0) {
                                pbVar44 = (byte *)((long)&local_578->_flags + 1);
                              }
                              dVar54 = strtod((char *)pbVar44,(char **)local_590);
                              if (((*piVar24 != 0) || (local_590[0] == pbVar44)) ||
                                 ((*local_590[0] != 0 ||
                                  ((dVar54 < -1.7976931348623157e+308 ||
                                   (1.7976931348623157e+308 < dVar54)))))) goto LAB_002672e0;
                              pcVar27 = (char *)((long)&local_578->_IO_read_base + 1);
                              if (((ulong)local_578->_IO_read_base & 1) != 0) {
                                pcVar27 = local_578->_IO_write_ptr;
                              }
                              pbVar44 = (byte *)(pcVar27 + -1);
                              do {
                                pbVar49 = pbVar44;
                                pbVar44 = pbVar49 + 1;
                                bVar41 = *pbVar44;
                              } while (bVar41 == 0x20 || 0xfffffffa < bVar41 - 0xe);
                              if (bVar41 == 0x30) {
                                bVar41 = pbVar49[2];
                                if (bVar41 == 0x78) {
                                  iVar16 = 0x10;
                                }
                                else {
                                  iVar16 = 0x10;
                                  if (bVar41 != 0x58) {
                                    iVar16 = 10;
                                  }
                                }
                              }
                              else {
                                iVar16 = 10;
                              }
                              *piVar24 = 0;
                              local_590[0] = (byte *)0x0;
                              lVar32 = strtoll((char *)pbVar44,(char **)local_590,iVar16);
                              if (*piVar24 != 0) goto LAB_002672e0;
                              if ((pbVar44 == local_590[0]) || (*local_590[0] != 0)) {
                                iVar16 = 0x16;
LAB_00267550:
                                *piVar24 = iVar16;
                                goto LAB_002672e0;
                              }
                              if (lVar32 != (int)lVar32) {
                                iVar16 = 0x22;
                                goto LAB_00267550;
                              }
                              (**(code **)(*(long *)param_6 + 0x130))
                                        ((float)(dVar54 * dVar8),(float)(int)lVar32,param_6);
                            }
                            else {
LAB_002672e0:
                              uVar19 = android::base::ShouldLog(4,0);
                              if ((uVar19 & 1) != 0) {
                                iVar16 = *piVar24;
                                android::base::LogMessage::LogMessage
                                          ((LogMessage *)local_590,
                                           "bootable/recovery/install/install.cpp",499,4,0,
                                           0xffffffff);
                                poVar35 = (ostream *)
                                          android::base::LogMessage::stream((LogMessage *)local_590)
                                ;
                                poVar35 = std::
                                          __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                                    (poVar35,"invalid \"progress\" parameters: ",
                                                     0x1f);
                                std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                                char_traits<char>,std::operator<(poVar35,(string *)&local_530);
                                android::base::LogMessage::~LogMessage((LogMessage *)local_590);
                                *piVar24 = iVar16;
                              }
                            }
                            pFVar37 = local_578;
                            pFVar29 = local_570;
                            if (local_578 != (FILE *)0x0) {
                              while (pFVar45 = pFVar29, pFVar45 != pFVar37) {
                                pFVar29 = (FILE *)&pFVar45[-1]._mode;
                                if ((*(int *)pFVar29 & 1) != 0) {
                                  operator_delete(*(void **)(pFVar45[-1]._unused2 + 0xc));
                                }
                              }
                              goto LAB_002673f4;
                            }
                          }
                          else {
                            if (*plVar30 != 0x746e6972705f6975) goto LAB_002670b0;
                            pvVar31 = (void *)((ulong)&local_560 | 1);
                            if ((local_560 & 1) != 0) {
                              pvVar31 = local_550;
                            }
                            (**(code **)(*(long *)param_6 + 0x160))(param_6,&DAT_001467f7,pvVar31);
                            fflush(*(FILE **)PTR_stdout_00429b30);
                          }
                        }
                        else {
                          if (pFVar46 == (FILE *)0xc) {
                            plVar30 = plVar51;
                            if (bVar41 != 0) {
                              plVar30 = local_538;
                            }
                            if (*plVar30 == 0x676f72705f746573 && (int)plVar30[1] == 0x73736572) {
                              local_578 = (FILE *)0x0;
                              local_570 = (FILE *)0x0;
                              local_568 = (void *)0x0;
                              local_590[0] = (byte *)((ulong)local_590[0]._3_5_ << 0x18);
                              local_590[0] = (byte *)CONCAT62(local_590[0]._2_6_,0x2002);
                              android::base::Split
                                        ((base *)&local_560,(string *)local_590,(string *)pFVar37);
                              if (((ulong)local_590[0] & 1) != 0) {
                                operator_delete(local_580);
                              }
                              if ((long)local_570 - (long)local_578 == 0x18) {
                                iVar16 = local_578->_flags;
                                pbVar44 = (byte *)local_578->_IO_read_end;
                                *piVar24 = 0;
                                local_590[0] = (byte *)0x0;
                                if ((iVar16 & 1) == 0) {
                                  pbVar44 = (byte *)((long)&local_578->_flags + 1);
                                }
                                dVar54 = strtod((char *)pbVar44,(char **)local_590);
                                if ((((*piVar24 != 0) || (local_590[0] == pbVar44)) ||
                                    (*local_590[0] != 0)) ||
                                   ((dVar54 < -1.7976931348623157e+308 ||
                                    (1.7976931348623157e+308 < dVar54)))) goto LAB_002673a0;
                                (**(code **)(*(long *)param_6 + 0x138))((float)dVar54,param_6);
                              }
                              else {
LAB_002673a0:
                                uVar19 = android::base::ShouldLog(4,0);
                                if ((uVar19 & 1) != 0) {
                                  iVar16 = *piVar24;
                                  android::base::LogMessage::LogMessage
                                            ((LogMessage *)local_590,
                                             "bootable/recovery/install/install.cpp",0x1fb,4,0,
                                             0xffffffff);
                                  poVar35 = (ostream *)
                                            android::base::LogMessage::stream
                                                      ((LogMessage *)local_590);
                                  poVar35 = std::
                                            __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                                      (poVar35,
                                                  "invalid \"set_progress\" parameters: ",0x23);
                                  std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                                  char_traits<char>,std::operator<(poVar35,(string *)&local_530);
                                  android::base::LogMessage::~LogMessage((LogMessage *)local_590);
                                  *piVar24 = iVar16;
                                }
                              }
                              pFVar37 = local_578;
                              pFVar29 = local_570;
                              if (local_578 == (FILE *)0x0) goto LAB_002673fc;
                              while (pFVar45 = pFVar29, pFVar45 != pFVar37) {
                                pFVar29 = (FILE *)&pFVar45[-1]._mode;
                                if ((*(int *)pFVar29 & 1) != 0) {
                                  operator_delete(*(void **)(pFVar45[-1]._unused2 + 0xc));
                                }
                              }
LAB_002673f4:
                              local_570 = pFVar37;
                              operator_delete(local_578);
                              goto LAB_002673fc;
                            }
                          }
                          else if (pFVar46 == (FILE *)0xd) {
                            plVar30 = plVar51;
                            if (bVar41 != 0) {
                              plVar30 = local_538;
                            }
                            if (*plVar30 == 0x69645f7261656c63 &&
                                *(long *)((long)plVar30 + 5) == 0x79616c707369645f) {
                              (**(code **)(*(long *)param_6 + 0x118))(param_6,0);
                              goto LAB_002673fc;
                            }
                          }
LAB_002670b0:
                          pFVar37 = (FILE *)(ulong)((byte)local_548._0_1_ >> 1);
                          if (((byte)local_548._0_1_ & 1) != 0) {
                            pFVar37 = local_540;
                          }
                          if (pFVar37 == (FILE *)0xc) {
                            plVar30 = plVar51;
                            if (((byte)local_548._0_1_ & 1) != 0) {
                              plVar30 = local_538;
                            }
                            if (*plVar30 == 0x70755f7972746572 && (int)plVar30[1] == 0x65746164) {
                              bVar7 = true;
                              goto LAB_002673fc;
                            }
                          }
                          else if (pFVar37 == (FILE *)0xd) {
                            plVar30 = plVar51;
                            if (((byte)local_548._0_1_ & 1) != 0) {
                              plVar30 = local_538;
                            }
                            if (*plVar30 == 0x725f656c62616e65 &&
                                *(long *)((long)plVar30 + 5) == 0x746f6f6265725f65) {
                              (**(code **)(*(long *)param_6 + 0x1c0))(param_6,1);
                              goto LAB_002673fc;
                            }
                          }
                          bVar15 = std::
                                   operator==[abi_nn180000_<char,std::char_traits<char>,std::allocator<char>>
                                             ((string *)&local_548,"log");
                          if (bVar15) {
                            uVar19 = local_560 >> 1 & 0x7f;
                            if ((local_560 & 1) != 0) {
                              uVar19 = local_558;
                            }
                            if (uVar19 == 0) {
                              uVar19 = android::base::ShouldLog(4,0);
                              if ((uVar19 & 1) != 0) {
                                iVar16 = *piVar24;
                                android::base::LogMessage::LogMessage
                                          ((LogMessage *)&local_578,
                                           "bootable/recovery/install/install.cpp",0x210,4,0,
                                           0xffffffff);
                                poVar35 = (ostream *)
                                          android::base::LogMessage::stream
                                                    ((LogMessage *)&local_578);
                                poVar35 = std::
                                          __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                                    (poVar35,"invalid \"log\" parameters: ",0x1a);
                                std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                                char_traits<char>,std::operator<(poVar35,(string *)&local_530);
                                goto LAB_00267254;
                              }
                            }
                            else {
                              std::vector<std::string,std::allocator<std::string>>::
                              push_back_abi_ne180000_
                                        ((vector<std::string,std::allocator<std::string>> *)param_3,
                                         (string *)&local_560);
                            }
                          }
                          else {
                            uVar19 = android::base::ShouldLog(4,0);
                            if ((uVar19 & 1) != 0) {
                              iVar16 = *piVar24;
                              android::base::LogMessage::LogMessage
                                        ((LogMessage *)&local_578,
                                         "bootable/recovery/install/install.cpp",0x213,4,0,
                                         0xffffffff);
                              poVar35 = (ostream *)
                                        android::base::LogMessage::stream((LogMessage *)&local_578);
                              poVar35 = std::
                                        __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                                  (poVar35,"unknown command [",0x11);
                              poVar35 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                                        char_traits<char>,std::operator<
                                                  (poVar35,(string *)&local_548);
                              std::
                              __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                        (poVar35,"]",1);
LAB_00267254:
                              android::base::LogMessage::~LogMessage((LogMessage *)&local_578);
                              *piVar24 = iVar16;
                            }
                          }
                        }
LAB_002673fc:
                        if ((local_560 & 1) != 0) {
                          operator_delete(local_550);
                        }
                        bVar41 = (byte)local_548._0_1_ & 1;
                        goto LAB_00267414;
                      }
                      pFVar29 = (FILE *)(local_530 >> 1 & 0x7f);
                      if ((local_530 & 1) != 0) {
                        pFVar29 = local_528;
                      }
                      pFVar46 = (FILE *)((long)pFVar29 - (long)pFVar45);
                      if (pFVar45 <= pFVar29) {
                        pcVar27 = pcVar36;
                        if ((local_530 & 1) != 0) {
                          pcVar27 = local_520;
                        }
                        if (pFVar46 < (FILE *)0xfffffffffffffff0) {
                          if (pFVar46 < (FILE *)0x17) {
                            local_578 = (FILE *)CONCAT71(local_578._1_7_,(char)((int)pFVar46 << 1));
                            pvVar31 = (void *)((ulong)&local_578 | 1);
                            if (pFVar29 != pFVar45) goto LAB_00266ee0;
                          }
                          else {
                            uVar19 = ((ulong)pFVar46 | 0xf) + 1;
                            pvVar31 = operator_new(uVar19);
                            local_578 = (FILE *)(uVar19 | 1);
                            local_570 = pFVar46;
                            local_568 = pvVar31;
LAB_00266ee0:
                            pFVar37 = pFVar46;
                            memmove(pvVar31,pcVar27 + (long)pFVar45,(size_t)pFVar46);
                            psVar20 = extraout_x1_02;
                          }
                          *(char *)((long)pvVar31 + (long)pFVar46) = '\0';
                          android::base::Trim<std::string&>((base *)&local_578,psVar20);
                          if (((ulong)local_578 & 1) != 0) {
                            operator_delete(local_568);
                          }
                          bVar41 = (byte)local_548._0_1_ & 1;
                          pFVar46 = (FILE *)(ulong)((byte)local_548._0_1_ >> 1);
                          goto LAB_00266f28;
                        }
                        goto LAB_00267e18;
                      }
                      goto LAB_00267e30;
                    }
LAB_00267414:
                    if (bVar41 != 0) {
                      operator_delete(local_538);
                    }
                    if ((local_530 & 1) != 0) {
                      operator_delete(local_520);
                    }
                  }
                  fclose(pFVar26);
                  uVar19 = android::base::ShouldLog(4,0);
                  puVar52 = PTR_android_fdsan_close_with_tag_00429ae8;
                  if ((uVar19 & 1) != 0) {
                    iVar16 = *piVar24;
                    android::base::LogMessage::LogMessage
                              ((LogMessage *)&local_548,"bootable/recovery/install/install.cpp",
                               0x218,4,0,0xffffffff);
                    poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_548)
                    ;
                    poVar35 = std::
                              __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                        (poVar35,"Try update binary took:",0x17);
                    lVar28 = gettime_ns();
                    std::to_string((double)(ulong)(lVar28 - lVar18) / 1000000000.0);
                    std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
                    operator<(poVar35,(string *)&local_530);
                    if ((local_530 & 1) != 0) {
                      operator_delete(local_520);
                    }
                    android::base::LogMessage::~LogMessage((LogMessage *)&local_548);
                    *piVar24 = iVar16;
                  }
                  local_548 = local_548 & 0xffffffff00000000;
                  waitpid(__pid,(int *)&local_548,0);
                  local_509 = 1;
                  std::condition_variable::notify_one();
                  std::thread::join();
                  if (bVar7) {
                    uVar47 = 5;
                  }
                  else if ((local_548 & 0x7f) == 0) {
                    if ((local_548 & 0xff00) != 0) {
                      uVar19 = android::base::ShouldLog(4,0);
                      if ((uVar19 & 1) == 0) goto LAB_00267750;
                      iVar16 = *piVar24;
                      android::base::LogMessage::LogMessage
                                ((LogMessage *)&local_530,"bootable/recovery/install/install.cpp",
                                 0x225,4,0,0xffffffff);
                      poVar35 = (ostream *)
                                android::base::LogMessage::stream((LogMessage *)&local_530);
                      poVar35 = std::
                                __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                          (poVar35,"Error in ",9);
                      poVar35 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                                char_traits<char>,std::operator<(poVar35,&local_4b0);
                      poVar35 = std::
                                __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                          (poVar35," (status ",9);
                      poVar35 = (ostream *)std::ostream::operator<<(poVar35,(uint)local_548._1_1_);
                      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar35,")",1);
LAB_00267d80:
                      android::base::LogMessage::~LogMessage((LogMessage *)&local_530);
                      uVar47 = 1;
                      goto LAB_00267de0;
                    }
LAB_00267768:
                    uVar47 = 0;
                  }
                  else if (((uint)local_548 + 1 & 0x7e) == 0) {
                    uVar19 = android::base::ShouldLog(6,0);
                    if ((uVar19 & 1) == 0) goto LAB_00267768;
                    iVar16 = *piVar24;
                    android::base::LogMessage::LogMessage
                              ((LogMessage *)&local_530,"bootable/recovery/install/install.cpp",
                               0x22c,6,0,0xffffffff);
                    poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_530)
                    ;
                    poVar35 = std::
                              __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                        (poVar35,"Invalid status code ",0x14);
                    std::ostream::operator<<(poVar35,(uint)local_548);
                    android::base::LogMessage::~LogMessage((LogMessage *)&local_530);
                    uVar47 = 0;
LAB_00267de0:
                    *piVar24 = iVar16;
                  }
                  else {
                    uVar19 = android::base::ShouldLog(4,0);
                    if ((uVar19 & 1) != 0) {
                      iVar16 = *piVar24;
                      android::base::LogMessage::LogMessage
                                ((LogMessage *)&local_530,"bootable/recovery/install/install.cpp",
                                 0x229,4,0,0xffffffff);
                      poVar35 = (ostream *)
                                android::base::LogMessage::stream((LogMessage *)&local_530);
                      poVar35 = std::
                                __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                          (poVar35,"Error in ",9);
                      poVar35 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::
                                char_traits<char>,std::operator<(poVar35,&local_4b0);
                      poVar35 = std::
                                __put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                          (poVar35," (killed by signal ",0x13);
                      poVar35 = (ostream *)std::ostream::operator<<(poVar35,(uint)local_548 & 0x7f);
                      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                                (poVar35,")",1);
                      goto LAB_00267d80;
                    }
LAB_00267750:
                    uVar47 = 1;
                  }
                  std::thread::~thread((thread *)&local_518);
                }
              }
              uVar48 = uStack_4c4;
              sVar2 = local_4c8;
              pbVar44 = (byte *)CONCAT44(uStack_4c4,
                                         CONCAT13(cStack_4c5,CONCAT21(uStack_4c7,local_4c8)));
              if (pbVar44 != (byte *)0x0) {
                pbVar50 = (byte *)CONCAT17(acStack_4bf[6],CONCAT61(acStack_4bf._0_6_,sStack_4c0));
                pbVar49 = pbVar44;
                if (pbVar50 != pbVar44) {
                  do {
                    pbVar49 = pbVar50 + -0x18;
                    if ((*pbVar49 & 1) != 0) {
                      operator_delete(*(void **)(pbVar50 + -8));
                    }
                    pbVar50 = pbVar49;
                  } while (pbVar49 != pbVar44);
                  pbVar49 = (byte *)CONCAT44(uStack_4c4,
                                             CONCAT13(cStack_4c5,CONCAT21(uStack_4c7,local_4c8)));
                }
                sStack_4c0 = sVar2;
                acStack_4bf._0_6_ = (undefined6)((ulong)pbVar44 >> 8);
                acStack_4bf[6] = (char)((uint)uVar48 >> 0x18);
                operator_delete(pbVar49);
              }
              if (((byte)local_4b0 & 1) != 0) {
                operator_delete((void *)CONCAT17(uStack_499,
                                                 CONCAT16(uStack_49a,CONCAT51(uStack_49f,cStack_4a0)
                                                         )));
              }
            }
            else {
              uVar19 = android::base::ShouldLog(4,0);
              if ((uVar19 & 1) == 0) {
                uVar47 = 2;
              }
              else {
                puVar34 = (undefined4 *)__errno();
                uVar48 = *puVar34;
                android::base::LogMessage::LogMessage
                          ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x189,4,
                           0,uVar48);
                poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
                std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar35,"Failed to create pipe for updater-recovery communication",0x38);
                android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
                uVar47 = 2;
                *puVar34 = uVar48;
              }
            }
            puVar34 = (undefined4 *)__errno();
            iVar16 = CONCAT13(uStack_505,local_509._1_3_);
            uVar48 = *puVar34;
            if (iVar16 != -1) {
              if (puVar52 == (undefined *)0x0) {
                close(iVar16);
              }
              else {
                uVar33 = android_fdsan_create_owner_tag(3,(long)&local_509 + 1);
                android_fdsan_close_with_tag(iVar16,uVar33);
              }
            }
            iVar16 = local_504;
            *puVar34 = uVar48;
            if (local_504 != -1) {
              if (puVar52 == (undefined *)0x0) {
                close(local_504);
              }
              else {
                uVar33 = android_fdsan_create_owner_tag(3,&local_504);
                android_fdsan_close_with_tag(iVar16,uVar33);
              }
            }
            *puVar34 = uVar48;
          }
          else {
            uVar19 = android::base::ShouldLog(4,0);
            if ((uVar19 & 1) == 0) {
LAB_00266548:
              uVar47 = 1;
            }
            else {
              puVar34 = (undefined4 *)__errno();
              uVar48 = *puVar34;
              android::base::LogMessage::LogMessage
                        ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x16f,4,0,
                         0xffffffff);
              poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
              std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                        (poVar35,"Denying OTA because it\'s SPL downgrade",0x26);
              android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
              uVar47 = 1;
              *puVar34 = uVar48;
            }
          }
          if ((local_500 & 1) != 0) {
            operator_delete(local_4f0);
          }
        }
        std::
        __tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
        ::destroy((__tree<std::__value_type<std::string,std::string>,std::__map_value_compare<std::string,std::__value_type<std::string,std::string>,std::less<std::string>,true>,std::allocator<std::__value_type<std::string,std::string>>>
                   *)&local_4e8,local_4e0);
        (**(code **)(*(long *)param_6 + 0x1c0))(param_6,1);
        (**(code **)(*(long *)param_6 + 0x158))(param_6,&DAT_00152dff);
        goto LAB_00267898;
      }
      uVar19 = android::base::ShouldLog(4,0);
      if ((uVar19 & 1) != 0) {
        puVar34 = (undefined4 *)__errno();
        uVar48 = *puVar34;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",599,4,0,
                   0xffffffff);
        poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar35,"secureboot check_identity failed.",0x21);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
        *puVar34 = uVar48;
      }
      android::base::StringPrintf("error: %d",&local_490,0x15);
      puVar23 = *(undefined8 **)(param_3 + 8);
      if (*(undefined8 **)(param_3 + 0x10) <= puVar23) {
        psVar20 = std::vector<std::string,std::allocator<std::string>>::
                  __push_back_slow_path<std::string>
                            ((vector<std::string,std::allocator<std::string>> *)param_3,&local_490);
        goto LAB_002662a0;
      }
LAB_002660c4:
      puVar23[2] = CONCAT17(uStack_479,
                            CONCAT16(uStack_47a,
                                     CONCAT24(uStack_47c,
                                              CONCAT13(cStack_47d,CONCAT21(uStack_47f,cStack_480))))
                           );
      puVar23[1] = CONCAT26(uStack_482,CONCAT51(uStack_487,cStack_488));
      *puVar23 = CONCAT44(iStack_48c,CONCAT22(uStack_48e,CONCAT11(cStack_48f,local_490)));
      *(undefined8 **)(param_3 + 8) = puVar23 + 3;
    }
    else {
      iVar16 = check_verify(DAT_0057b3f8,this);
      if (-1 < iVar16) {
        if (iVar16 == 0) {
          uVar19 = android::base::ShouldLog(4,0);
          if ((uVar19 & 1) == 0) {
            bVar14 = true;
          }
          else {
            puVar34 = (undefined4 *)__errno();
            uVar48 = *puVar34;
            android::base::LogMessage::LogMessage
                      ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x26b,4,0,
                       0xffffffff);
            poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
            std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                      (poVar35,"Ignore verify.",0xe);
            android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
            bVar14 = true;
            *puVar34 = uVar48;
          }
        }
        else {
          bVar14 = false;
        }
        goto LAB_002660f4;
      }
      uVar19 = android::base::ShouldLog(4,0);
      if ((uVar19 & 1) != 0) {
        puVar34 = (undefined4 *)__errno();
        uVar48 = *puVar34;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)&local_490,"bootable/recovery/install/install.cpp",0x263,4,0,
                   0xffffffff);
        poVar35 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_490);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar35,"signature check_verify failed.",0x1e);
        android::base::LogMessage::~LogMessage((LogMessage *)&local_490);
        *puVar34 = uVar48;
      }
      android::base::StringPrintf("error: %d",&local_490,0x15);
      puVar23 = *(undefined8 **)(param_3 + 8);
      if (puVar23 < *(undefined8 **)(param_3 + 0x10)) goto LAB_002660c4;
      psVar20 = std::vector<std::string,std::allocator<std::string>>::
                __push_back_slow_path<std::string>
                          ((vector<std::string,std::allocator<std::string>> *)param_3,&local_490);
LAB_002662a0:
      *(string **)(param_3 + 8) = psVar20;
      if (((byte)local_490 & 1) != 0) {
        operator_delete((void *)CONCAT17(uStack_479,
                                         CONCAT16(uStack_47a,
                                                  CONCAT24(uStack_47c,
                                                           CONCAT13(cStack_47d,
                                                                    CONCAT21(uStack_47f,cStack_480))
                                                          ))));
      }
    }
    signal(7,(__sighandler_t)0x0);
    iVar16 = (**(code **)(*(long *)param_1 + 0x38))(param_1);
    uVar47 = 9;
    if ((this != (MemMapping *)0x0) && (iVar16 == 1)) {
      MemMapping::~MemMapping(this);
      operator_delete(this);
    }
  }
LAB_00267898:
  if (*(long *)(lVar6 + 0x28) == local_88) {
    return uVar47;
  }
LAB_002683d8:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
